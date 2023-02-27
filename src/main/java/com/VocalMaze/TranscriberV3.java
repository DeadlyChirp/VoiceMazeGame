package com.VocalMaze;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.transcribe.AmazonTranscribe;
import com.amazonaws.services.transcribe.AmazonTranscribeClientBuilder;
import com.amazonaws.services.transcribe.model.LanguageCode;
import com.amazonaws.services.transcribe.model.Media;
import com.amazonaws.services.transcribe.model.MediaFormat;
import com.amazonaws.services.transcribe.model.StartTranscriptionJobRequest;
import com.amazonaws.services.transcribe.model.StartTranscriptionJobResult;
import com.amazonaws.services.transcribe.model.Transcript;
import com.amazonaws.services.transcribe.model.TranscriptionJob;
import java.util.Random;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;

public class TranscriberV3 {

    String result; 

    TranscriberV3(String path) throws IOException, InterruptedException, ExecutionException, URISyntaxException{
        result = Transcriber(path); //path du fichier WAV
    }

    // public static void main(String[] args) throws IOException, InterruptedException, ExecutionException, URISyntaxException{
    //     TranscriberV3 test = new TranscriberV3("src/main/java/com/VocalMaze/Records/Audio.wav");
    //     System.out.println(test.result);
    // }
   
   public String Transcriber(String path) throws IOException, InterruptedException, ExecutionException, URISyntaxException {

        // Configurer les  AWS Credentials
        String accessKey = "AKIAVXTIJKHOAUWCIXFF";
        String secretKey = "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";
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
        
        // Creation d'un Amazon Transcribe client
        AmazonTranscribe transcribeClient = AmazonTranscribeClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .withRegion(Regions.EU_WEST_3)
                .build();

        //Création du  Amazon S3 client pour transférer le fichier vers le cloud
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
        .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
        .withRegion(Regions.EU_WEST_3)
        .build();
       
        // On ecrit le nom du job et le language de traduction
        char a = ' ';
        String b = "";
        Random rd = new Random();
        for (int i = 0; i< 10; i++) {
            a = (char) rd.nextInt(97, 123);
            b += a;
        }
        String jobName = b; //le nom du travail de transcription est généré de manière aléatoire
        LanguageCode languageCode = LanguageCode.FrFR;

        //On choisis le format du fichier auudio
        MediaFormat mediaFormat = MediaFormat.Wav;

        // On tranfère le fichier audio dans un bucket dont le nom est aléatoire
        String bucketName = "vocalmaze";
        String keyName = b;
        String filePath = path; //chemin vers l'audio local
        try {
            File file = new File(filePath);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("audio/mpeg");
            PutObjectRequest request = new PutObjectRequest(bucketName, keyName, file)
                    .withMetadata(metadata);
            s3Client.putObject(request);
            //System.out.println("Upload réussi");
        } catch (AmazonServiceException e) {
            e.printStackTrace();
        } catch (SdkClientException e) {
            e.printStackTrace();
        }

        Media media = new Media().withMediaFileUri("s3://vocalmaze/" + keyName);
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
        
                String newString = "";
                String[] words = transcriptText.split("\\s+");

                for (String word : words) {
                    if (word.contains("haut") || word.contains("bas") || word.contains("gauche") || word.contains("droite")) {
                        newString += word + " ";
                    }
                }
          
                return ("\nMOT DE DEPLACEMENT TROUVE : " + newString + "\n\n" + "TRADUCTION INITIAL : " + transcriptText);
       
   
    }
}