import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.transcribe.AmazonTranscribe;
import com.amazonaws.services.transcribe.AmazonTranscribeClient;
import com.amazonaws.services.transcribe.model.Media;
import com.amazonaws.services.transcribe.model.StartTranscriptionJobRequest;
import com.amazonaws.services.transcribe.model.StartTranscriptionJobResult;
import com.amazonaws.services.transcribe.model.Transcript;
import com.amazonaws.services.transcribe.model.TranscriptionJob;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Transcriber {

    public static void main(String[] args) throws IOException {

        String jobName = "projet-de-prog";
        String mediaFileUri = "s3://projectbucket/audio.mp3";
        String languageCode = "fr-FR";

        AWSCredentials credentials = null;
        try {
            credentials = new ProfileCredentialsProvider().getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException("Cannot load the credentials from the credential profiles file. "
                    + "Please make sure that your credentials file is at the correct "
                    + "location (~/.aws/credentials), and is in valid format.", e);
        }

        AmazonTranscribe transcribe = AmazonTranscribeClient.builder()
                .withCredentials(new ProfileCredentialsProvider())
                .withRegion(Regions.US_EAST_1)
                .build();

        Media media = new Media();
        media.setMediaFileUri(mediaFileUri);

        StartTranscriptionJobRequest request = new StartTranscriptionJobRequest()
                .withTranscriptionJobName(jobName)
                .withMedia(media)
                .withLanguageCode(languageCode);

        StartTranscriptionJobResult result = transcribe.startTranscriptionJob(request);

        while (true) {
            TranscriptionJob transcriptionJob = transcribe.getTranscriptionJob(
                    new com.amazonaws.services.transcribe.model.GetTranscriptionJobRequest()
                            .withTranscriptionJobName(jobName));
            if (transcriptionJob.getTranscriptionJobStatus().equals("COMPLETED")) {
                String transcriptFileUri = transcriptionJob.getTranscript().getTranscriptFileUri();
                String transcript = readTranscriptFile(transcriptFileUri);
                System.out.println("Transcription: " + transcript);
                break;
            } else if (transcriptionJob.getTranscriptionJobStatus().equals("FAILED")) {
                System.out.println("Transcription echouée");
                break;
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static String readTranscriptFile(String transcriptFileUri) throws IOException {
        AmazonS3 s3Client = AmazonS3Client.builder()
                .withCredentials(new ProfileCredentialsProvider())
                .withRegion(Regions.US_EAST_1)
                .build();
        S3Object s3object = s3Client.getObject(transcriptFileUri);
        S3ObjectInputStream inputStream = s3object.getObjectContent();
    
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                Transcript transcript = Transcript.fromJson(line);
                for (Transcript.TranscriptItem transcriptItem : transcript.getResults().getTranscripts().get(0).getTranscriptItems()) {
                    stringBuilder.append(transcriptItem.getAlternatives().get(0).getTranscript());
                    stringBuilder.append(" ");
                }
            }
            return stringBuilder.toString().trim();
        }
    }

}
