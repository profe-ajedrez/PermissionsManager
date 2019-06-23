package com.example.skeleton.lib.runnables;


public abstract class RunnableArgs implements Runnable {
    protected Object[] mArgs;

    public void run(Object... args) {
        mArgs = args;
        run();
    }

    public int getArgCount() {
        return mArgs == null ? 0 : mArgs.length;
    }

    public Object[] getArgs() {
        return mArgs;
    }
}
