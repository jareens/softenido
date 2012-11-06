/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softenido.cafecore.benchmark;

import com.softenido.cafecore.gauge.GaugeView;
import com.softenido.cafecore.gauge.PrintStreamGaugeView;
import com.softenido.cafecore.gauge.ProxyGaugeProgress;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author franci
 */
public class BenchmarkManager implements Runnable
{
    static long NANOS_PER_MILLIS = 1000000L;    
    static int MILLIS = 10000;
    static int UPDATE_MILLIS = 333;
    static int UNITS = 1000;
    final String name;
    final int millis;
    double average;
    final int mediaRate;
    
    final ProxyGaugeProgress progress = new ProxyGaugeProgress();
    ArrayList<Benchmark> benchmarks = new ArrayList<Benchmark>();
    Map<String,Benchmark> map = new HashMap<String,Benchmark>();
    volatile boolean canceled=false;
    final CyclicBarrier barrier;
    private final int threads;

    public BenchmarkManager(String name, int millis, int mediaRate)
    {
        this.name = name;
        this.millis = millis;
        this.mediaRate=mediaRate;
        this.threads = Runtime.getRuntime().availableProcessors();
        this.barrier= new CyclicBarrier(this.threads+1);
    }
    public BenchmarkManager(String name, int millis)
    {
        this(name, millis, UNITS);
    }
    public static void main(String[] args)
    {
        GaugeView gp = new PrintStreamGaugeView(System.out);
        BenchmarkManager manager = BenchmarkManager.buildDefault(MILLIS, UNITS);
        manager.setView(gp);
        manager.run();
        manager.print(System.out);
    }
    
    @SuppressWarnings("SleepWhileInLoop")
    public final void run()
    {
        canceled=false;
        
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        try
        {
            Benchmark[] tests = this.benchmarks.toArray(new Benchmark[0]);
            progress.start(millis*tests.length, name);
            long nanos = millis*NANOS_PER_MILLIS;
            double avg=0;

            for(int i=0;i<tests.length && !canceled;i++)
            {
                progress.setPrefix(tests[i].getName());
                progress.setVal(millis*i);
                try
                {
                    barrier.reset();
                    long elapsed;
                    tests[i].start();
                    for(int t=0;t<threads;t++)
                    {
                        executor.execute(tests[i]);
                    }
                    barrier.await();
                    long initiated = System.nanoTime();
                    while( (elapsed=System.nanoTime()-initiated)<nanos  && !canceled)
                    {
                        long wn = Math.min(nanos-elapsed, UPDATE_MILLIS*NANOS_PER_MILLIS);
                        if(wn>0)
                        {
                            long ms = (wn/NANOS_PER_MILLIS);
                            int ns = (int)(wn%NANOS_PER_MILLIS);
                            Thread.sleep(ms,ns);
                        }
                        progress.setVal((int)(millis*i+elapsed/NANOS_PER_MILLIS));
                    }                        
                    tests[i].stop();
                    barrier.await();
                    progress.setVal(millis*(i+1));
                }
                catch (BrokenBarrierException ex)
                {
                    Logger.getLogger(BenchmarkManager.class.getName()).log(Level.SEVERE, null, ex);
                }
                catch (InterruptedException ex)
                {
                    Logger.getLogger(BenchmarkManager.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.gc();
                avg += tests[i].getRate();
            }
            average = avg/tests.length;
            progress.setVal(millis*tests.length);
            progress.close();
        }
        finally
        {
            executor.shutdown();
        }
        
    }
    
    public static BenchmarkManager buildDefault(int millis, int units)
    {
        BenchmarkManager manager = new BenchmarkManager("default",millis);
        manager.add(new NewBenchMark(units, manager.barrier));
        manager.add(new IntegerBenchMark(units, manager.barrier));
        manager.add(new LongBenchMark(units, manager.barrier));
        manager.add(new StringBenchMark(units, manager.barrier));
        manager.add(new FloatBenchMark(units, manager.barrier));
        manager.add(new DoubleBenchMark(units, manager.barrier));
        manager.add(new SqrtBenchMark(units, manager.barrier));
        manager.add(new BigIntegerBenchMark(units, manager.barrier));
        manager.add(new MD5BenchMark(units, manager.barrier));
        manager.add(new SHA1BenchMark(units, manager.barrier));
        return manager;
    }
    public static BenchmarkManager buildDefault(int millis)
    {
        return buildDefault(millis, UNITS);
    }

    public void add(Benchmark benchmark)
    {
        this.benchmarks.add(benchmark);
        this.map.put(benchmark.getName(),benchmark);
    }

    public String getName()
    {
        return name;
    }

    public String print()
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(baos);
        print(out);
        return out.toString();
    }
    public void print(PrintStream out)
    {
        out.println();
        out.println("-----------------------------------------------------------\n");
        out.printf("| %-16s = %-10.2f                          |\n", this.name, this.average);
        out.println("-----------------------------------------------------------\n");
        out.println("|      name        |   count    | count/sec  |    rate    |\n");
        out.println("-----------------------------------------------------------\n");
        for(Benchmark bm : benchmarks)
        {
            int c = bm.getCount();
            double r = bm.getRate();
            double cs= (c*1000.0)/millis;
            out.printf("| %-16s | %10d | %10.2f | %10.2f |\n", bm.getName(), c, cs, r);
        }
        out.println("-----------------------------------------------------------\n");
    }
    public double getAverage()
    {
        return average;
    }
    public Benchmark[] getBenchmarks()
    {
        return this.benchmarks.toArray(new Benchmark[0]);
    }
    public Benchmark getBenchmark(String name)
    {
        return this.map.get(name);
    }

    public void setView(GaugeView view)
    {
        progress.setView(view);
    }
    public void cancel()
    {
        this.canceled = true;
        for(Benchmark bm : benchmarks)
        {
            bm.stop();
        }
    }
    public boolean isCanceled()
    {
        return canceled;
    }
}
