package ar.frba.utn;

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

    public double calcularSenalDeControl(double cargaActual, double deltaTiempo) {
        double error = valorNominal - cargaActual;
        integral += error * deltaTiempo;
        double derivativo = (error - errorPrevio) / deltaTiempo;

        double senalDeControl = kp * error + ki * integral + kd * derivativo;
        errorPrevio = error;

        return senalDeControl;
    }

    public static void main(String[] args) {
        SistemaDeControlDeCargaDeCPU controlador = new SistemaDeControlDeCargaDeCPU(1.0, 0.1, 0.05, 75.0);

        double cargaActual = 40.0; // Carga inicial de CPU
        double deltaTiempo = 1.0; // Duracion de Scan

        for (int i = 0; i < 100; i++) {
            double senalDeControl = controlador.calcularSenalDeControl(cargaActual, deltaTiempo);
            cargaActual += senalDeControl * deltaTiempo; // Simular el efecto de la senal de control sobre la carga de CPU

            // Simulacion de perturbaciones (e.g., carga de trabajo variante, cambio de temperatura)
            cargaActual += (Math.random() - 0.5) * 2;

            System.out.printf("T: %d, Carga CPU: %.2f\n", i, cargaActual);

            // Simular el tiempo de scan
            try {
                Thread.sleep((long) (deltaTiempo * 1000));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}