package nfa;

import java.util.List;
import java.util.Random;

public interface ExecutableNode {
    boolean canExecute() throws InterruptedException;

    int executeNode() throws InterruptedException; //return anything < 0 to stop script

    List<Edge> getAdjacentNodes();

    void logNode();

    default int randomNormalDist(double mean, double stddev){
        return Math.abs((int) (new Random().nextGaussian() * stddev + mean));
    }
}
