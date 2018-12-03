package nfa;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MarkovNodeExecutor {

    private final HashMap<Class<? extends ExecutableNode>, ExecutableNode> classTypeMap;
    private ExecutableNode current;

    public MarkovNodeExecutor(ExecutableNode startingNode, ExecutableNode... nodes){
        current = startingNode;
        classTypeMap = new HashMap<>(16);
        classTypeMap.put(startingNode.getClass(), startingNode);
        for(ExecutableNode n: nodes){
            classTypeMap.put(n.getClass(), n);
        }
    }

    /*
    returns the sleeptime until the next onLoop call.
    inside onloop there should be a line such as:
    return executor.executeThenTraverse();
    where executor is an instance of this class
    sleep times returns are implemented inside the executeNode() method (which returns an int) in each ExecutableNode instance
     */
    public int executeThenTraverse() throws InterruptedException {
        int onLoopSleepTime = 500;
        if(current.canExecute()){
            onLoopSleepTime = current.executeNode();
        }
        normalTraverse();
        return onLoopSleepTime;
    }

    public ExecutableNode getCurrent() {
        return current;
    }

    public void setCurrent(ExecutableNode current) {
        this.current = current;
    }

    private void normalTraverse(){
        if(current != null){
            List<Edge> edges = current.getAdjacentNodes();
            if(edges == null || edges.size() == 0){
                throw new NullPointerException("no outgoing edges off " + current.getClass().getSimpleName());
            }
            // Algorithm for random percentage branching
            // https://stackoverflow.com/questions/45836397/coding-pattern-for-random-percentage-branching?noredirect=1&lq=1
            int combinedWeight = edges.stream().mapToInt(Edge::getExecutionWeight).sum();
            int sum = 0;
            int roll = ThreadLocalRandom.current().nextInt(1, combinedWeight+1);
            ExecutableNode selected = null;
            for(Edge e: edges){
                sum += e.getExecutionWeight();
                if(sum >= roll){
                    selected = classTypeMap.get(e.getV());
                    break;
                }
            }
            if(selected == null){
                selected = classTypeMap.get(edges.get(edges.size()-1).getV());
            }
            current = selected;
        }
    }
}