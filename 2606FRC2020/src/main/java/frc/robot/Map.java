package frc.robot;

//Robot Name should be McCree

public class Map{
    
    //Can/PWM/PCM/PID addresses for Motor controllers and Solinoides 
    //CAN(Controller Area Network) -- PWM (Pulse Width Modulation)  
    //PCM (Pneumatics Control Module) -- PID (Proportional Integral Derivative)
        //Drive Train
        public static final int frontRightTalon = 1; //CAN
            public static final int frontRightEncoder = 0; //PID
        public static final int frontLeftTalon = 8; //CAN
            public static final int frontLeftEncoder = 1; //PID
        public static final int backRightVictor = 2; //CAN
        public static final int backLeftVictor = 7; //CAN
        
        //Intake
        public static final int intake = 5; //can 
        
        //Indexer (Revolver)
        public static final int revolver = 3; //CAN
        
        //Fly Wheel
        public static final int flyMotor = 6; //CAN
        public static final int loader = 1; //PWM
        //Lift
        public static final int liftUp = 4; //CAN
        public static final int pullDown = 0; // pwm
        

    //Usb constant for flight stick usb usally 0 and 1 
    public static final int leftFLUsb = 0;
    public static final int rightFLUsb = 1;

    //Quick reverse
    public static final boolean reverse = false;

    //Change max speed
    public static final double speedMax = 0.8;

    /* Nonzero to block the config until success, zero to skip checking */
    public static final int kTimeoutMs = 30;

    //Controls
    public static final int turnRevolverButton = 2;
    public static final int shootAllBalls = 3;
    public static final int aim = 4;
    public static final int climbUp = 8;
    public static final int climbDown = 9;

    //Subsystem speeds
    public static final double climbSpeed = 0.2;
    public static final double inSpeed = 0.5;
}