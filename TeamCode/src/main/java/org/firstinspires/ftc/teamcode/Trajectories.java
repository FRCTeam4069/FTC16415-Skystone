package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.path.PathBuilder;
import com.acmerobotics.roadrunner.path.QuinticSpline;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryGenerator;
import com.acmerobotics.roadrunner.trajectory.constraints.DriveConstraints;

public class Trajectories {

    public static final DriveConstraints CONSTRAINTS = new DriveConstraints(69, 69, 69, 69, 69, 69);

    public static final Trajectory xd = TrajectoryGenerator.INSTANCE.generateTrajectory(new PathBuilder(new Pose2d())
            .splineTo(new Pose2d(15, 15, 0))
            .splineTo(new Pose2d(30, 16, 0))
            .build(), CONSTRAINTS);
}
