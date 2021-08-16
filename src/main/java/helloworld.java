import org.postgresql.pljava.annotation.Function;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.ArrayList;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackInputStream;

import uk.ac.starlink.table.StarTable;
import uk.ac.starlink.table.StarTableOutput;
import uk.ac.starlink.task.Task;
import uk.ac.starlink.task.TaskException;
import uk.ac.starlink.ttools.task.MapEnvironment;
import uk.ac.starlink.ttools.task.TablePipe;
import uk.ac.starlink.ttools.task.CdsUploadSkyMatch;
import uk.ac.starlink.table.ColumnInfo;
import uk.ac.starlink.table.TableFormatException;
import uk.ac.starlink.table.formats.RowEvaluator;
import uk.ac.starlink.table.formats.StreamStarTable;
import uk.ac.starlink.util.DataSource;


public class helloworld {
    @Function
    public static String hello(String toWhom) {
        return "Hello, " + toWhom + "!";
    }

    public static StarTable stiltsCommand(String command)
            throws TaskException, IOException {

        String[] cmds = command.split(" ", 0);
        if( cmds.length < 1) {
            System.err.println("\n Error. No args: " + Main.class.getName());
            // System.exit(1);
        }

        MapEnvironment env = new MapEnvironment();

        for(String cmd : cmds) {
            String[] cmdSplit = cmd.split("=", 0);
            System.out.println(Arrays.toString(cmdSplit));
            switch (cmdSplit[0]) {
                case "cdstable" -> env.setValue("cdstable", cmdSplit[1]);
                case "find" -> env.setValue("find", cmdSplit[1]);
                case "ifmt" -> env.setValue("ifmt", cmdSplit[1]);
                case "ra" -> env.setValue("ra", cmdSplit[1]);
                case "dec" -> env.setValue("dec", cmdSplit[1]);
                case "radius" -> env.setValue("radius", cmdSplit[1]);
                case "in" -> env.setValue("in", cmdSplit[1]);
                case "out" -> env.setValue("out", cmdSplit[1]);
            }
        }

        Task cdsskyTask = new CdsUploadSkyMatch();
        cdsskyTask.createExecutable(env).execute();

        StarTable outTable = env.getOutputTable("omode");

        return outTable;
    }

    @Function
    public static void cdsskymatch(String[] args) throws TaskException, IOException {
        // Get stilts command
        if( args.length < 1) {
            System.err.println("\n Error. No args: " + Main.class.getName());
            System.exit(1);
        }
        /*
        cdstable=I/350/gaiaedr3
        find=all
        ifmt=csv
        in=Keck_2021-08-09_UT-CandidatesForObservation-HighPriorityCandidates.csv
        ra=RA
        dec=Dec
        radius=4
        out=TargetsWithGaiaEDR3counterparts.csv*/

        System.out.println(Arrays.toString(args));

        // Set up and populate execution environ w/ param values
        MapEnvironment env = new MapEnvironment();

        for(String arg : args) {
            String[] argSplit = arg.split("=", 0);
            System.out.println(Arrays.toString(argSplit));
            switch (argSplit[0]) {
                case "cdstable" -> env.setValue("cdstable", argSplit[1]);
                case "find" -> env.setValue("find", argSplit[1]);
                case "ifmt" -> env.setValue("ifmt", argSplit[1]);
                case "ra" -> env.setValue("ra", argSplit[1]);
                case "dec" -> env.setValue("dec", argSplit[1]);
                case "radius" -> env.setValue("radius", argSplit[1]);
                case "in" -> env.setValue("in", argSplit[1]);
                case "out" -> env.setValue("out", argSplit[1]);
            }
        }

        // Create and execute task
        Task cdsskyTask = new CdsUploadSkyMatch();
        cdsskyTask.createExecutable(env).execute();

        StarTable outTable = env.getOutputTable("omode");

        // TODO: Not sure if csv is valid format...
        new StarTableOutput().writeStarTable(outTable, "starOutput","ascii");

    }
}
