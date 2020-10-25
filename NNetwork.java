/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.openbot;

/**
 *
 * @author Nikita
 */
public class NNetwork {
            public int inodes;
        public int hnodes;
        public int onodes;
        public float learnrate;
        public double[][] wih;
        public double[][] who;
    public NNetwork(int in,int h,int o,float lr){
            this.inodes = in;
            this.hnodes = h;
            this.onodes = o;
            this.learnrate = lr;
            this.wih = new double[h][in];
            this.who = new double[o][h];
            for(int i=0;i<h;i++)
                for(int j=0;j<in;j++)
                    this.wih[i][j] = Math.random() - 0.5;
            for(int i=0;i<o;i++)
                for(int j=0;j<h;j++)
                    this.who[i][j] = Math.random() - 0.5;
    }
    public void train(double[] inputs_list,double[] targets_list){
        double[][] inputs = new double[inputs_list.length][1];
        for(int i=0;i<inputs_list.length;i++)
            inputs[i][0] = inputs_list[i];
        double[][] targets = new double[targets_list.length][1];
        for(int i=0;i<targets_list.length;i++)
            targets[i][0] = targets_list[i];
        double[][] hidden_inputs = multi(this.wih,inputs);
        double[][] hidden_outputs = activation_func(hidden_inputs);
        double[][] final_inputs = multi(this.who,hidden_outputs);
        double[][] final_outputs = activation_func(final_inputs);
        double[][] output_errors = minus(targets,final_outputs);
        double[][] hidden_errors = multi(transponate(this.who),output_errors);
        double[][] mho1 = multi_el(output_errors,final_outputs);
        double[][] mho2 = one_minus(final_outputs);
        double[][] mho3 = multi_el(mho1,mho2);
        double[][] mho4 = multi(mho3,transponate(hidden_outputs));
        this.who = plus(this.who,multipile(mho4,this.learnrate));
        double[][] mih1 = multi_el(hidden_errors,hidden_outputs);
        double[][] mih2 = one_minus(hidden_outputs);
        double[][] mih3 = multi_el(mih1,mih2);
        double[][] mih4 = multi(mih3,transponate(inputs));
        this.wih = plus(this.wih,multipile(mih4,this.learnrate));
    }
    public double[][] query(double[] inputs_list){
        double[][] inputs = new double[inputs_list.length][1];
        for(int i=0;i<inputs_list.length;i++)
            inputs[i][0] = inputs_list[i];
        double[][] hidden_inputs = multi(this.wih,inputs);
        double[][] hidden_outputs = activation_func(hidden_inputs);
        double[][] final_inputs = multi(this.who,hidden_outputs);
        double[][] final_outputs = activation_func(final_inputs);
        return final_outputs;
    }
    private double[][] multi_el(double a[][],double b[][]){
        double[][] res = new double[a.length][a[0].length];
        for(int i=0;i<a.length;i++)
            for(int j=0;j<a[0].length;j++)
                res[i][j] = a[i][j] * b[i][j];
        return res;
    }
    private double[][] multi(double a[][],double b[][]){
        int m = a.length;
        int n = b[0].length;
        int o = b.length;
        double[][] res = new double[m][n];
        for(int i=0;i<m;i++)
            for(int j=0;j<n;j++)
                for(int k=0;k<o;k++)
                    res[i][j] += a[i][k] * b[k][j];
        return res;
    }
    private double[][] plus(double a[][],double b[][]){
        double[][] res = new double[a.length][a[0].length];
        for(int i=0;i<a.length;i++)
            for(int j=0;j<a[0].length;j++)
                res[i][j] = a[i][j] + b[i][j];
        return res;
    }
    private double[][] minus(double a[][],double b[][]){
        double[][] res = new double[a.length][a[0].length];
        for(int i=0;i<a.length;i++)
            for(int j=0;j<a[0].length;j++)
                res[i][j] = a[i][j] - b[i][j];
        return res;
    }
    private double[][] one_minus(double a[][]){
        double[][] res = new double[a.length][a[0].length];
        for(int i=0;i<a.length;i++)
            for(int j=0;j<a[0].length;j++)
                res[i][j] = 1 -a[i][j];
        return res;
    }
    private double[][] multipile(double a[][],double m){
        double[][] res = new double[a.length][a[0].length];
        for(int i=0;i<a.length;i++)
            for(int j=0;j<a[0].length;j++)
                res[i][j] = a[i][j] * m;
        return res;
    }
    public void matrixout(double a[][]){
            for(int i=0;i<a.length;i++){
                for(int j=0;j<a[0].length;j++){
                    System.out.print(a[i][j] + "   ");
                }
                System.out.println();
            }
    }
    private static double sigmoida(double x){
        return 1/(1 + Math.exp(-x));
    }
    private static double[][] activation_func(double a[][]){
        double[][] res = new double[a.length][a[0].length];
        for(int i=0;i<a.length;i++)
            for(int j=0;j<a[0].length;j++)
                res[i][j] = sigmoida(a[i][j]);
        return res;
    }
    private double[][] transponate(double a[][]){
        double[][] res = new double[a[0].length][a.length];
        for(int i=0;i<a.length;i++)
            for(int j=0;j<a[0].length;j++)
                res[j][i] = a[i][j];
        return res;
    }
    
}
