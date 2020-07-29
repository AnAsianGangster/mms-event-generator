import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

class EventGenerator {

    private static int numOfBool = 0;
    private static int numOfInt = 0;
    private static int numOfUtc = 0;
    private static int numOfOct = 0;
    private static int numOfUnsigned = 0;
    private static int numOfVisible = 0;
    private static int numOfBit = 0;
    private static int numOfFloat = 0;

    private static String getBifDataString(String attributeName, String dataType, int lineCounter){
        String returnString = "";
        switch (dataType) {
            case "bool":
                returnString = "                                      " + attributeName + "_for_csv_line_" + lineCounter;
                break;
        
            case "int":
                returnString = "                                      " + attributeName + "_for_csv_line_" + lineCounter;
                break;
        
            case "utc_time":
                returnString = "                                      string_to_val(" + attributeName + "_for_csv_line_" + lineCounter + ")";
                break;
        
            case "octet_string":
                returnString = "                                      string_to_val(" + attributeName + "_for_csv_line_" + lineCounter + ")";
                break;
        
            case "unsigned_int":
                returnString = "                                      " + attributeName + "_for_csv_line_" + lineCounter;
                break;
        
            case "visible_string":
                returnString = "                                      string_to_val(" + attributeName + "_for_csv_line_" + lineCounter + ")";
                break;
        
            case "bit_string":
                returnString = "                                      string_to_val(" + attributeName + "_for_csv_line_" + lineCounter + ")";
                break;
        
            case "float":  // as int for now
                returnString = "                                      " + attributeName + "_for_csv_line_" + lineCounter;
                break;
        
            default:
                returnString = "                                      -1";
                break;
        }
        return returnString;
    }

    private static String getEventDataString(String attributeName, String dataType, int lineCounter){
        String returnString = "";
        switch (dataType) {
            case "bool":
                returnString = attributeName + "_for_csv_line_" + lineCounter + ": " + "bool";
                break;
        
            case "int":
                returnString = attributeName + "_for_csv_line_" + lineCounter + ": " + "int";
                break;
        
            case "utc_time":
                returnString = attributeName + "_for_csv_line_" + lineCounter + ": " + "string";
                break;
        
            case "octet_string":
                returnString = attributeName + "_for_csv_line_" + lineCounter + ": " + "stirng";
                break;
        
            case "unsigned_int":
                returnString = attributeName + "_for_csv_line_" + lineCounter + ": " + "count";
                break;
        
            case "visible_string":
                returnString = attributeName + "_for_csv_line_" + lineCounter + ": " + "string";
                break;
        
            case "bit_string":
                returnString = attributeName + "_for_csv_line_" + lineCounter + ": " + "string";
                break;
        
            case "float":  // as int for now
                returnString = attributeName + "_for_csv_line_" + lineCounter + ": " + "int";
                break;
        
            default:
                returnString = "error: int";
                break;
        }
        return returnString;
    }

    private static String getDataString(String attributeName, String dataType, int lineCounter) {
        String returnString = "";
        switch (dataType) {
            case "bool":
                returnString = "bool " + attributeName + "_for_csv_line_" + lineCounter
                        + " = (this->connection()->upflow()->data_map[item_id_from_csv_line_" + lineCounter
                        + "][0].first == \"true\") ? true : false;";
                break;

            case "int":
                returnString = attributeName + "_for_csv_line_" + lineCounter
                        + " = std::stoi(this->connection()->upflow()->data_map[item_id_from_csv_line_" + lineCounter
                        + "][0].first);";
                break;

            case "utc_time":
                returnString = attributeName + "_for_csv_line_" + lineCounter
                        + " = this->connection()->upflow()->data_map[item_id_from_csv_line_" + lineCounter
                        + "][0].first;";
                break;

            case "octet_string":
                returnString = attributeName + "_for_csv_line_" + lineCounter
                        + " = this->connection()->upflow()->data_map[item_id_from_csv_line_" + lineCounter
                        + "][0].first;";
                break;

            case "unsigned_int":
                returnString = attributeName + "_for_csv_line_" + lineCounter
                        + " = std::stoi(this->connection()->upflow()->data_map[item_id_from_csv_line_" + lineCounter
                        + "][0].first);";
                break;

            case "visible_string":
                returnString = attributeName + "_for_csv_line_" + lineCounter
                        + " = this->connection()->upflow()->data_map[item_id_from_csv_line_" + lineCounter
                        + "][0].first;";
                break;

            case "bit_string":
                returnString = attributeName + "_for_csv_line_" + lineCounter
                        + " = this->connection()->upflow()->data_map[item_id_from_csv_line_" + lineCounter
                        + "][0].first;";
                break;

            case "float": // as int for now
                returnString = attributeName + "_for_csv_line_" + lineCounter
                        + " = std::stoi(this->connection()->upflow()->data_map[item_id_from_csv_line_" + lineCounter
                        + "][0].first);";
                break;

            default:
                returnString = "cerr << \"unkown data type\"" + " << endl;";
                break;
        }
        return returnString;
    }

    public static void main(String[] args) {

        String analyzerFilename = "../ResiGate/src/analyzer/protocol/mms/mms-analyzer-test.pac";
        String eventFilename = "../ResiGate/src/analyzer/protocol/mms/event_test.bif";
        String scriptFilename = "../ResiGate/scripts/base/protocols/mms/auto_events.bro";

        String fileName = "user_configuration.csv";
        File file = new File(fileName);

        try {
            // write analyzer
            FileWriter analyzerWriter = new FileWriter(analyzerFilename);
            PrintWriter analyzerOutputStream = new PrintWriter(analyzerWriter);
            Scanner inputStream = new Scanner(file);
            analyzerOutputStream.println("refine flow MMS_Flow += {");
            analyzerOutputStream.println("  function rule_function():bool");
            analyzerOutputStream.println("  %{");

            // write event
            FileWriter eventWriter = new FileWriter(eventFilename);
            PrintWriter eventOutputStream = new PrintWriter(eventWriter);

            // write scripts
            FileWriter scriptWriter = new FileWriter(scriptFilename);
            PrintWriter scriptOutputStream = new PrintWriter(scriptWriter);
            scriptOutputStream.println("module mms_test;");
            scriptOutputStream.println();
            scriptOutputStream.println("redef enum Log::ID += { LOG };");
            scriptOutputStream.println();
            scriptOutputStream.println("type Info: record {");
            scriptOutputStream.println("    ## Timestamp for when the event happened.");
            scriptOutputStream.println("    ts:     time    &log;");
            scriptOutputStream.println("    ## Unique ID for the connection.");
            scriptOutputStream.println("    uid:    string  &log;");
            scriptOutputStream.println("    ## The connection's 4-tuple of endpoint addresses/ports.");
            scriptOutputStream.println("    id:     conn_id &log;");
            scriptOutputStream.println();
            scriptOutputStream.println("    # ## TODO: Add other fields here that you'd like to log.");
            scriptOutputStream.println("    Message_type: string &log;");
            scriptOutputStream.println("    invoke_id: int &log;");
            scriptOutputStream.println("    boolean_result: bool &log;");
            scriptOutputStream.println("};");
            scriptOutputStream.println();
            scriptOutputStream.println("global log_mms: event(rec: Info);");
            scriptOutputStream.println();
            scriptOutputStream.println("event bro_init() &priority=5{");
            scriptOutputStream.println("	Log::create_stream(LOG, [$columns=Info, $ev=log_mms, $path=\"mms_test\"]);");
            scriptOutputStream.println("}");
            scriptOutputStream.println();

            // attributes (file first line)
            String attributeNames[] = inputStream.nextLine().split(",");

            // zeek target data
            int lineCounter = 2;
            while (inputStream.hasNext()) {
                String data = inputStream.nextLine();
                String[] values = data.split(",");

                // analyzer_test.pac
                // for (int i = 0; i < attributeNames.length; i++) {
                //     analyzerOutputStream.println("    " + "string " + attributeNames[i] + "_from_csv_line_" + lineCounter + " = \"" + values[i] + "\";");
                // }
                analyzerOutputStream.println("    " + "string " + attributeNames[0] + "_from_csv_line_" + lineCounter + " = \"" + values[0] + "\";");
                // analyzerOutputStream.println("    " + values[2] + " result_data_for_csv_line_" + lineCounter + ";");
                analyzerOutputStream.println("    if(data_map.find(\"" + values[0] + "\") != data_map.end()){");
                analyzerOutputStream.println("      cout << \"\\x1B[36m\" << \"[MAP DATA]\" << \"\\033[0m\" << this->connection()->upflow()->data_map[item_id_from_csv_line_" + lineCounter + "][0].first << endl;");
                analyzerOutputStream.println("      " + getDataString("result_data", values[2], lineCounter));
                analyzerOutputStream.println("      BifEvent::generate_auto_event_" + lineCounter + "(connection()->bro_analyzer(),");
                analyzerOutputStream.println("                                      connection()->bro_analyzer()->Conn(),");
                analyzerOutputStream.println("                                      this->connection()->upflow()->invoke_id,");
                analyzerOutputStream.println(getBifDataString("result_data", values[2], lineCounter));
                analyzerOutputStream.println("                                      );");
                analyzerOutputStream.println("    }");
                analyzerOutputStream.println();

                // event_test.bif TODO other data type
                eventOutputStream.println("##");
                eventOutputStream.println("##");
                eventOutputStream.println("##");
                eventOutputStream.println("##");
                eventOutputStream.println("##");
                eventOutputStream.print("event auto_event_"+ lineCounter + "%(c: connection, invoke_id: int, ");
                eventOutputStream.print(getEventDataString("result_data", values[2], lineCounter));
                eventOutputStream.print("%);" + "\n");

                // auto_events.bro
                scriptOutputStream.println("event auto_event_" + lineCounter + "(c: connection, invoke_id: int, " + getEventDataString("result_data", values[2], lineCounter) + ") {");
                scriptOutputStream.println("    local info: Info;");
                scriptOutputStream.println("	info$ts = network_time();");
                scriptOutputStream.println("	info$uid = c$uid;");
                scriptOutputStream.println("	info$id = c$id;");
                scriptOutputStream.println("	info$Message_type = \"auto events\";");
                scriptOutputStream.println("	info$invoke_id = invoke_id;");
                scriptOutputStream.println("	info$boolean_result = " + "result_data_for_csv_line_"+ lineCounter + ";");
                scriptOutputStream.println("	Log::write(LOG, info);");
                scriptOutputStream.println("    print \"[SCRIPT EVENT] \" , " + "result_data_for_csv_line_" + lineCounter + " , invoke_id;");
                scriptOutputStream.println("}");
                scriptOutputStream.println();
                lineCounter++;
            }

            // analyzer
            analyzerOutputStream.println("    return false;");
            analyzerOutputStream.println("  %}");
            analyzerOutputStream.println("}");
            analyzerOutputStream.close();

            // event
            eventOutputStream.close();

            // script
            scriptOutputStream.close();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
