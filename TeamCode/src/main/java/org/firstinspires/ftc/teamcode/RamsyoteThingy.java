package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.followers.RamseteFollower;
import com.acmerobotics.roadrunner.trajectory.TrajectoryGenerator;

public class RamsyoteThingy {

    public void xd() {
        RamseteFollower follower = new RamseteFollower(2.0, 0.7);
        follower.followTrajectory(Trajectories.xd);

    }
}
