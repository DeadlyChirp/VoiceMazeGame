package com.VocalMaze;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutionException;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.transcribe.AmazonTranscribe;
import com.amazonaws.services.transcribe.AmazonTranscribeClientBuilder;
import com.amazonaws.services.transcribe.model.LanguageCode;
import com.amazonaws.services.transcribe.model.Media;
import com.amazonaws.services.transcribe.model.MediaFormat;
import com.amazonaws.services.transcribe.model.StartTranscriptionJobRequest;
import com.amazonaws.services.transcribe.model.StartTranscriptionJobResult;
import com.amazonaws.services.transcribe.model.Transcript;
import com.amazonaws.services.transcribe.model.TranscriptionJob;
import com.amazonaws.services.transcribe.model.TranscriptionJobStatus;
import java.util.Base64;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;



public class TranscriberV3 {
    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {

        // Configurer les  AWS Credentials
        String accessKey = "AKIAVXTIJKHOAUWCIXFF";
        String secretKey = "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";
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
        
        // Creation d'un Amazon Transcribe client
        AmazonTranscribe transcribeClient = AmazonTranscribeClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .withRegion(Regions.EU_WEST_3)
                .build();

       
        // On ecrit le nom du job et le language de traduction
        String jobName = "vocalmaze5";
        LanguageCode languageCode = LanguageCode.FrFR;

        //On choisis le format du fichier auudio
        MediaFormat mediaFormat = MediaFormat.Wav;

        // On place le fichier audio
        Media media = new Media().withMediaFileUri("s3://vocalmaze/Audio(1).wav");

        // On met en place la requete pour la traduction
        StartTranscriptionJobRequest transcriptionJobRequest = new StartTranscriptionJobRequest()
                .withMedia(media)
                .withMediaFormat(mediaFormat)
                .withTranscriptionJobName(jobName)
                .withLanguageCode(languageCode);

        // On commence la traduction
        StartTranscriptionJobResult transcriptionJobResult = transcribeClient.startTranscriptionJob(transcriptionJobRequest);
        String transcriptionJobName = transcriptionJobResult.getTranscriptionJob().getTranscriptionJobName();

        // On verifie le statut de la traduction périodiquement
        String jobStatus = null;
        do {
            Thread.sleep(5000);
            TranscriptionJob transcriptionJob = transcribeClient.getTranscriptionJob(
                    new com.amazonaws.services.transcribe.model.GetTranscriptionJobRequest()
                    .withTranscriptionJobName(transcriptionJobName)).getTranscriptionJob();
            jobStatus = transcriptionJob.getTranscriptionJobStatus();
            System.out.println("Statut de la traduction: " + jobStatus);
        } while (jobStatus.equals("IN_PROGRESS"));

        // On récupère le resultat de la traduction
        Transcript transcriptionResult = transcribeClient.getTranscriptionJob(
                new com.amazonaws.services.transcribe.model.GetTranscriptionJobRequest()
                .withTranscriptionJobName(transcriptionJobName)).getTranscriptionJob().getTranscript();

                String transcriptFileUri = transcriptionResult.getTranscriptFileUri();

                String transcriptText = "";
                try {
                    URL transcriptUrl = new URL(transcriptFileUri);
                    BufferedReader in = new BufferedReader(new InputStreamReader(transcriptUrl.openStream()));
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        JsonObject jsonObject = new JsonParser().parse(inputLine).getAsJsonObject();
                        transcriptText += jsonObject.get("results").getAsJsonObject().get("transcripts").getAsJsonArray().get(0).getAsJsonObject().get("transcript").getAsString();
                    }
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("\n\nTRADUCTION : " + transcriptText);
   
    }
}