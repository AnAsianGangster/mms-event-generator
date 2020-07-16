import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

class EventGenerator {
    public static void main(String[] args) {

        String analyzerFilename = "../ResiGate/src/analyzer/protocol/mms/mm-analyzer-test.pac";
        String eventFilename = "../ResiGate/src/analyzer/protocol/mms/event-test.bif";

        try {
            // write analyzer
            FileWriter analyzerWriter = new FileWriter(analyzerFilename);
            PrintWriter analyzerOutputStream = new PrintWriter(analyzerWriter);
            
            analyzerOutputStream.println("refine flow MMS_Flow += {");
            analyzerOutputStream.println("  function rule_function(itemID: string, index: int):bool");
            analyzerOutputStream.println("  %{");
            analyzerOutputStream.println("    // attr_value_string = map[{$itemID}][index]");
            analyzerOutputStream.println("    // attr_value = cast_string_to_original_type();");
            analyzerOutputStream.println("    // attr_value_string = map[{$itemID}][index]");
            analyzerOutputStream.println("    // attr_value = cast_string_to_original_type();");
            analyzerOutputStream.println("    // BifEvent::generate_mms_event_<i>(bro_analyzer(),");
            analyzerOutputStream.println("    //                                  bro_analyzer()->Conn(),");
            analyzerOutputStream.println("    //                                  attr_value_string,");
            analyzerOutputStream.println("    //                                  attr_value);");
            analyzerOutputStream.println("    return true;");
            analyzerOutputStream.println("  %}");
            analyzerOutputStream.println("}");
            analyzerOutputStream.close();

            // write event
            FileWriter eventWriter = new FileWriter(eventFilename);
            PrintWriter eventOutputStream = new PrintWriter(eventWriter);

            eventOutputStream.println("event mms_event_test%(c: connection, itemID:string, res:bool%");
            eventOutputStream.close();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
