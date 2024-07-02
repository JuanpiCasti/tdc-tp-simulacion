package ar.frba.utn;

public class CpuLoadControlSystem {

    private double kp; // Proportional gain
    private double ki; // Integral gain
    private double kd; // Derivative gain

    private double setPoint; // Desired CPU load
    private double integral;
    private double previousError;

    public CpuLoadControlSystem(double kp, double ki, double kd, double setPoint) {
        this.kp = kp;
        this.ki = ki;
        this.kd = kd;
        this.setPoint = setPoint;
        this.integral = 0;
        this.previousError = 0;
    }

    public double compute(double currentLoad, double deltaTime) {
        double error = setPoint - currentLoad;
        integral += error * deltaTime;
        double derivative = (error - previousError) / deltaTime;

        double output = kp * error + ki * integral + kd * derivative;
        previousError = error;

        return output;
    }

    public static void main(String[] args) {
        CpuLoadControlSystem controller = new CpuLoadControlSystem(1.0, 0.1, 0.05, 75.0);

        double currentLoad = 40.0; // Initial CPU load
        double deltaTime = 1.0; // Time step in seconds

        for (int i = 0; i < 100; i++) {
            double controlSignal = controller.compute(currentLoad, deltaTime);
            currentLoad += controlSignal * deltaTime; // Simulate the effect of the control signal on the load

            // Simulate external perturbations (e.g., varying workload)
            currentLoad += (Math.random() - 0.5) * 2;

            System.out.printf("Time: %d, CPU Load: %.2f\n", i, currentLoad);

            // Sleep to simulate real-time passage (for demonstration purposes)
            try {
                Thread.sleep((long) (deltaTime * 1000));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}