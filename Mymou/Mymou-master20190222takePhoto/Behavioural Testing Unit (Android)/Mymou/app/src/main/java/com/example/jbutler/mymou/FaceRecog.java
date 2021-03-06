package com.example.jbutler.mymou;

import android.os.Environment;
import android.util.Log;

import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;


/**
 Uses opencsv parser library使用opencsv解析器库
    Download from http://opencsv.sourceforge.net/
 Which in turn requires Apache Commons Lang 3.6 and Commons BeanUtils
    Download from http://commons.apache.org/proper/commons-beanutils/index.html and
    http://commons.apache.org/proper/commons-lang/download_lang.cgi respectively
 Place the three .jar file in libs folder
 将三个.jar文件放在libs文件夹中
 Build --> Edit Libraries and Dependencies --> Add opencsv JAR
 */

final class FaceRecog {

    double[][] wi, wo;
    double mean, var;

    public FaceRecog() {
        wi = loadWeights("wi.txt");
        wo = loadWeights("wo.txt");
        double[][] meanAndVar = loadWeights("meanAndVar.txt");
        mean = meanAndVar[0][0];
        var = meanAndVar[1][0];
        Log.d("faceRecog",mean+" "+var);
    }

    public final double[][] loadWeights(String fileName) {
        // Read all读取所有；Environment:Provides access to environment variables.
        String path= Environment.getExternalStorageDirectory().getAbsolutePath() + "/Mymou/" +
                fileName;
        try {
            CSVReader csvReader = new CSVReader(new FileReader(new File(path)));
            List<String[]> list = csvReader.readAll();
            // Convert to 2D array
            double[][] dataArr = convertStringListToFloatArray(list);
            return dataArr;
        } catch (IOException e) {
            //TODO: No file present
        }

        Log.d("faceRecog","Couldn't open CSV files");
        return null;
    }

    private final double[][] convertStringListToFloatArray(List<String[]> stringList) {
        //Convert List String to String matrix
        String[][] stringArray = new String[stringList.size()][];
        stringArray = stringList.toArray(stringArray);

        //Then convert String matrix to Double Matrix
        int rows = stringArray.length;
        int columns = stringArray[0].length;
        Log.d("faceRecog",""+rows+" "+columns);
        double[][] doubleArray = new double[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j ++) {
                try {
                    doubleArray[i][j] = Double.parseDouble(stringArray[i][j]);
                } catch (NumberFormatException e) {
                  Log.d("faceRecog",e+" "+stringArray[i][j]);
                }
            }
        }
        return doubleArray;
    }

    //Convert 1D int array to 2D double array
    //Also adds extra point to the array
    public static double[][] convertIntToDoubleArray(int[] source) {
        int length = source.length;
        double[][] dest = new double[1][length + 1];
        for(int i=0; i<length; i++) {
            dest[0][i] = source[i];
        }

        //Add extra point at end to compensate for bias weight   在末尾添加额外的点以补偿偏置重量
        dest[0][length] = 0;

        return dest;
    }

    // 通过将均值和设置方差减去1来标准化数组
    // Standardise array by subtracting mean and setting variance to 1
    private double[][] standardiseArray(double[][] input) {
        double[][] output = new double[input.length][input[0].length];
        int rows = input.length;
        int columns = input[0].length;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j ++) {
                output[i][j] = (input[i][j] - mean) / var;
            }
        }
        return output;
    }

    public int idImage(int[] input) {
        //Convert photo int array to double array   将photo int数组转换为double数组
        double[][] doubleArray = convertIntToDoubleArray(input);

        //Subtract training mean and variance      减去训练均值和方差
        double[][] standardisedArray = standardiseArray(doubleArray);

        // Hidden activations
        double[][] sumHidden = MatrixMaths.dot(standardisedArray, wi);
        double[][] activationHidden = MatrixMaths.tanh(sumHidden);

        // Output activations
        double[][] sumOutput = MatrixMaths.dot(activationHidden, wo);
        double[][] activationOutput = MatrixMaths.softmax(sumOutput);

        // Convert to bool
        if (activationOutput[0][0] > activationOutput[0][1]) {
            return 1;  // Monkey O
        } else {
            return 0;  // Monkey V
        }
    }

}



