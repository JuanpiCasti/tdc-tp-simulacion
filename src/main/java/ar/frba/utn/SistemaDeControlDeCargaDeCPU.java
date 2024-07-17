package ar.frba.utn;

import java.util.Locale;

public class SistemaDeControlDeCargaDeCPU {

    private double kp; // Ganancia proporcional
    private double ki; // Ganancia integral
    private double kd; // Ganancia derivativa

    private double valorNominal; // Carga de CPU buscada
    private double integral;
    private double errorPrevio;

    public SistemaDeControlDeCargaDeCPU(double kp, double ki, double kd, double valorNominal) {
        this.kp = kp;
        this.ki = ki;
        this.kd = kd;
        this.valorNominal = valorNominal;
        this.integral = 0;
        this.errorPrevio = 0;
    }

    public double calcularError(double cargaActual) {
        return valorNominal - cargaActual;
    }
    public double calcularSenalDeControl(double error, double deltaTiempo) {
        integral += error * deltaTiempo;
        double derivativo = (error - errorPrevio) / deltaTiempo;

        double senalDeControl = kp * error + ki * integral + kd * derivativo;
        errorPrevio = error;

        return senalDeControl;
    }

    public double simularPerturbacion(double cargaActual) {
        if(Math.random() < 0.1) {
            double perturbacionMax = 100 - cargaActual;
            return Math.random() * perturbacionMax;
        }
            return 0;
    }

    public String colorPrint(double perturbacion) {
        String amarillo = "\u001B[33m";
        String reset = "\u001B[0m";
        if(perturbacion > 0)
            return amarillo;
        return reset;
    }

    public static void main(String[] args) {
        double kp = 0.8;
        double ki = 0.2;
        double kd = 0.1;
        SistemaDeControlDeCargaDeCPU controlador = new SistemaDeControlDeCargaDeCPU(kp, ki, kd, 75.0);
        double cargaActual = 50; // Carga inicial de CPU
        double deltaTiempo = 1.0; // Duracion de Scan
        double error = 0;
        double perturbacion = 0;
        String colorReset = "\u001B[0m";


        System.out.println("Seteo de valores");
        System.out.println("KP: " + kp + "\nKI: " + ki + "\nKD: " + kd);
        System.out.println("Valor nominal: "  +controlador.valorNominal);
        System.out.println("Carga inicial del sistema: " +  cargaActual + " %");
        System.out.println("--------------------------------------------------");
        System.out.println("Primera iteracion");
        System.out.println("T: 0, Señal de error: " + error +  " Carga CPU medida: " + cargaActual + "%");
        System.out.println("--------------------------------------------------\n");


        for (int i = 1; i < 100; i++) {
            error = controlador.calcularError(cargaActual);
            double senalDeControl = controlador.calcularSenalDeControl(error, deltaTiempo);

            // Simular el tiempo de scan
            try {
                Thread.sleep((long) (deltaTiempo * 1000));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            cargaActual += senalDeControl * deltaTiempo; // Simular el efecto de la senal de control sobre la carga de CPU
            perturbacion = controlador.simularPerturbacion(cargaActual);// Simulacion de perturbaciones (e.g., carga de trabajo variante, cambio de temperatura)
            cargaActual += perturbacion;
            String color = controlador.colorPrint(perturbacion);

            System.out.printf(Locale.US,color + "T: %d, Señal de error: %.2f, Carga CPU medida: %.2f %%\n", i, error,cargaActual);
        }
    }
}