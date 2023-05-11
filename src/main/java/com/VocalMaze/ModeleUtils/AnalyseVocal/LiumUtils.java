package com.VocalMaze.ModeleUtils.AnalyseVocal ; 

import fr.lium.spkDiarization.lib.MainTools;
import fr.lium.spkDiarization.libClusteringData.Cluster;
import fr.lium.spkDiarization.libClusteringData.ClusterSet;
import fr.lium.spkDiarization.libFeature.AudioFeatureSet;
import fr.lium.spkDiarization.parameter.Parameter;
import fr.lium.spkDiarization.parameter.ParameterBNDiarization;
import fr.lium.spkDiarization.parameter.ParameterModel;
import fr.lium.spkDiarization.parameter.ParameterSegmentation;
import fr.lium.spkDiarization.system.Diarization;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Objects;

public class LiumUtils {

    public static Result executeForResult(String audioFile) throws Exception {
        if (audioFile == null) {
            throw new IllegalArgumentException("audioFile must not be null");
        }

        File file = new File(audioFile);
        if (!file.exists()) {
            throw new IllegalArgumentException("audioFile is not exists");
        }

        AudioFileFormat format = AudioSystem.getAudioFileFormat(file);
        if (!format.getType().equals(AudioFileFormat.Type.WAVE)) {
            throw new IllegalArgumentException(format.getType() + " is not supported");
        }

        return executeForResult(new String[]{"--fInputMask=" + audioFile});
    }

    public static void executeCommand(String command) {
        if (command == null) {
            return;
        }

        execute(command.split("\\s+"));
    }

    public static Result resultFromSegment(String segmentFile) {
        if (segmentFile == null) {
            throw new IllegalArgumentException("segmentFile must not be null");
        }

        File file = new File(segmentFile);
        if (!file.exists()) {
            throw new IllegalArgumentException("segmentFile is not exists");
        }

        Result result = new Result();
        try (BufferedReader bufferedReader = Files.newBufferedReader(file.toPath())) {
            String line;
            boolean flag = false;

            while ((line = bufferedReader.readLine()) != null) {
                if (!flag) {
                    if (line.contains("cluster") && line.contains("score")) {
                        flag = true;
                    }
                } else {
                    flag = false;
                    result.increase(line.split("\\s+")[4]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    private static void execute(final String[] args) {
        if (args == null || args.length == 0) {
            return;
        }

        Diarization.main(args);
    }

    private static Result executeForResult(final String[] args) throws Exception  {
        Diarization diarization = new Diarization();
        Result result = new Result();
        final Parameter parameter = Diarization.getParameter(args);
        StringBuilder featuresDescriptionBuilder = new StringBuilder("audio16Khz2sphinx:sphinx,1:1:0:0:0:0,18,0:0:0:0");
        parameter.getParameterInputFeature().setFeaturesDescription(featuresDescriptionBuilder.toString());
        if (Objects.equals(parameter.getParameterDiarization().getSystem(), ParameterBNDiarization.SystemString[1])) {
            parameter.getParameterSegmentationSplit().setSegmentMaximumLength(10 * parameter.getParameterSegmentationInputFile().getRate());
        }
        final ClusterSet fullClusterSet = diarization.initialize(parameter);
        ArrayList<ClusterSet> clusterSets = MainTools.splitHypotesis(fullClusterSet);
        for (ClusterSet clusterSet : clusterSets) {
            final String featureDesc = parameter.getParameterInputFeature().getFeaturesDescriptorAsString();
            AudioFeatureSet featureSet = Diarization.loadFeature(parameter, clusterSet, featureDesc);
            featureSet.setCurrentShow(parameter.show);
            final int nbFeatures = featureSet.getNumberOfFeatures();
            clusterSet.getFirstCluster().firstSegment().setLength(nbFeatures);
            ClusterSet clustersSegInit = diarization.sanityCheck(clusterSet, featureSet, parameter);
            final int segmentationMethod = parameter.getParameterSegmentation().getMethod().ordinal();
            final String segmentationMethodString = ParameterSegmentation.SegmentationMethodString[segmentationMethod];
            final ClusterSet clustersSegSave = diarization.segmentation(segmentationMethodString, ParameterModel.KindTypeString[0], clustersSegInit, featureSet, parameter);
            final ClusterSet clustersSeg = clustersSegSave.clone();
            final ClusterSet clustersLClust = diarization.clusteringLinear(2, clustersSeg, featureSet, parameter);
            final ClusterSet clustersHClust = diarization.clustering(3, clustersLClust, featureSet, parameter);
            final ClusterSet clustersDClust = diarization.decode(8, 250, clustersHClust, featureSet, parameter);
            final ClusterSet clustersSplitClust = diarization.speech("10,10,50", clusterSet, clustersSegInit, clustersDClust, featureSet, parameter);
            final ClusterSet clustersGender = diarization.gender(clusterSet, clustersSplitClust, featureSet, parameter);

            if (!clustersGender.clusterSetValue().isEmpty()) {
                for (Cluster cluster : clustersGender.clusterSetValue()) {
                    result.increase(cluster.getGender());
                }
            }
        }

        return result;
    }

    //Test LiumUtil seulement, pas d'utilisation dans le jeu
    public static void main(String[] args) throws Exception {
        System.setProperty("java.util.logging.config.file", "src/main/java/logging.properties");

        args = new String[]{
                "--fInputMask=test/test(3).wav",
        };

        String filename = "audio.wav";

        Result result = new Result();
        try {
            result = executeForResult(filename);
        } catch (Exception e) {
            e.printStackTrace();
        }

        executeCommand("--fInputMask=" + filename + " --sOutputMask=test.seg --doCEClustering show");

        System.out.println("1: " + result);
        System.out.println("2: " + resultFromSegment("test.seg"));
        System.out.println("Done");
    }
    
    public static class Result {
        public int unknown = 0;
        public int male = 0;
        public int female = 0;

        public Result() {
            unknown = 0;
            male = 0;
            female = 0;
        }

        public void increase(String gender) {
            if (Cluster.genderStrings[1].equals(gender)) {
                male++;
            } else if (Cluster.genderStrings[2].equals(gender)) {
                female++;
            } else if (Cluster.genderStrings[0].equals(gender) || Cluster.genderStrings[3].equals(gender)) {
                unknown++;
            }
        }

        @Override
        public String toString() {
            return "Result{" + "unknown=" + unknown + ", male=" + male + ", female=" + female + '}';
        }

    }
    
}