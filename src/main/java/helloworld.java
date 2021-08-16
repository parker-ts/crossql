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

    @Function
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
}
