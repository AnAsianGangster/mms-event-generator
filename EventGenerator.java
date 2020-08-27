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

    private static String buildBifDataString(String dataType, int lineCounter){
        String returnString = "";
        switch (dataType) {
            case "BOOLEAN":
                returnString = "                                      " + "num_" + numOfBool + "_bool" + "_for_csv_line_" + lineCounter;
                break;
        
            case "INT32U":
                returnString = "                                      " + "num_" + numOfInt + "_int" + "_for_csv_line_" + lineCounter;
                break;
        
            case "TIMESTAMP":
                returnString = "                                      string_to_val(" + "num_" + numOfUtc + "_utc" + "_for_csv_line_" + lineCounter + ")";
                break;
        
            case "VISSTRING65":
                returnString = "                                      string_to_val(" + "num_" + numOfOct + "_oct" + "_for_csv_line_" + lineCounter + ")";
                break;
        
            case "INT8":
                returnString = "                                      " + "num_" + numOfUnsigned + "_unsigned" + "_for_csv_line_" + lineCounter;
                break;
        
            case "VISSTRING255":
                returnString = "                                      string_to_val(" + "num_" + numOfVisible + "_visible" + "_for_csv_line_" + lineCounter + ")";
                break;
        
            case "QUALITY":
                returnString = "                                      string_to_val(" + "num_" + numOfBit + "_bit" + "_for_csv_line_" + lineCounter + ")";
                break;
        
            case "FLOAT32":  // as int for now
                returnString = "                                      " + "num_" + numOfFloat + "_float" + "_for_csv_line_" + lineCounter;
                break;
        
            default:
                returnString = "                                      -1";
                break;
        }
        return returnString;
    }

    private static String buildEventDataString(String dataType, int lineCounter){
        String returnString = "";
        switch (dataType) {
            case "BOOLEAN":
                returnString = "num_" + numOfBool + "_bool" + "_for_csv_line_" + lineCounter + ": " + "bool";
                break;
        
            case "INT32U":
                returnString = "num_" + numOfInt + "_int" + "_for_csv_line_" + lineCounter + ": " + "int";
                break;
        
            case "TIMESTAMP":
                returnString = "num_" + numOfUtc + "_utc" + "_for_csv_line_" + lineCounter + ": " + "string";
                break;
        
            case "VISSTRING65":
                returnString = "num_" + numOfOct + "_oct" + "_for_csv_line_" + lineCounter + ": " + "stirng";
                break;
        
            case "INT8":
                returnString = "num_" + numOfUnsigned + "_unsigned" + "_for_csv_line_" + lineCounter + ": " + "count";
                break;
        
            case "VISSTRING255":
                returnString = "num_" + numOfVisible + "_visible" + "_for_csv_line_" + lineCounter + ": " + "string";
                break;
        
            case "QUALITY":
                returnString = "num_" + numOfBit + "_bit" + "_for_csv_line_" + lineCounter + ": " + "string";
                break;
        
            case "FLOAT32":  // as int for now
                returnString = "num_" + numOfFloat + "_float" + "_for_csv_line_" + lineCounter + ": " + "int";
                break;
        
            default:
                returnString = "error: int";
                break;
        }
        return returnString;
    }

    private static String buildScriptDataString(String dataType, int lineCounter){
        String returnString = "";
        switch (dataType) {
            case "BOOLEAN":
                returnString = "num_" + numOfBool + "_bool" + "_for_csv_line_" + lineCounter;
                break;
        
            case "INT32U":
                returnString = "num_" + numOfInt + "_int" + "_for_csv_line_" + lineCounter;
                break;
        
            case "TIMESTAMP":
                returnString = "num_" + numOfUtc + "_utc" + "_for_csv_line_" + lineCounter;
                break;
        
            case "VISSTRING65":
                returnString = "num_" + numOfOct + "_oct" + "_for_csv_line_" + lineCounter;
                break;
        
            case "INT8":
                returnString = "num_" + numOfUnsigned + "_unsigned" + "_for_csv_line_" + lineCounter;
                break;
        
            case "VISSTRING255":
                returnString = "num_" + numOfVisible + "_visible" + "_for_csv_line_" + lineCounter;
                break;
        
            case "QUALITY":
                returnString = "num_" + numOfBit + "_bit" + "_for_csv_line_" + lineCounter;
                break;
        
            case "FLOAT32":  // as int for now
                returnString = "num_" + numOfFloat + "_float" + "_for_csv_line_" + lineCounter;
                break;
        
            default:
                returnString = "error: int";
                break;
        }
        return returnString;
    }

    private static String buildDataString(String dataType, int lineCounter) {
        String returnString = "";
        switch (dataType) {
            case "BOOLEAN":
                numOfBool++;
                returnString = "    bool " + "num_" + numOfBool + "_bool" + "_for_csv_line_" + lineCounter + " = (this->connection()->upflow()->data_map[item_id_from_csv_line_" + lineCounter + "][0].first == \"true\") ? true : false;";
                break;

            case "INT32U":
                numOfInt++;
                returnString = "    int " + "num_" + numOfInt + "_int" + "_for_csv_line_" + lineCounter + " = std::stoi(this->connection()->upflow()->data_map[item_id_from_csv_line_" + lineCounter + "][0].first);";
                break;

            case "TIMESTAMP":
                numOfUtc++;
                returnString = "    string " + "num_" + numOfUtc + "_utc" + "_for_csv_line_" + lineCounter + " = this->connection()->upflow()->data_map[item_id_from_csv_line_" + lineCounter + "][0].first;";
                break;

            case "VISSTRING65":
                numOfOct++;
                returnString = "    string " + "num_" + numOfOct + "_oct" + "_for_csv_line_" + lineCounter + " = this->connection()->upflow()->data_map[item_id_from_csv_line_" + lineCounter + "][0].first;";
                break;

            case "INT8":
                numOfUnsigned++;
                returnString = "    unsigned " + "num_" + numOfUnsigned + "_unsigned" + "_for_csv_line_" + lineCounter + " = std::stoi(this->connection()->upflow()->data_map[item_id_from_csv_line_" + lineCounter + "][0].first);";
                break;

            case "VISSTRING255":
                numOfVisible++;
                returnString = "    string " + "num_" + numOfVisible + "_visible" + "_for_csv_line_" + lineCounter + " = this->connection()->upflow()->data_map[item_id_from_csv_line_" + lineCounter + "][0].first;";
                break;

            case "QUALITY":
                numOfBit++;
                returnString = "    string " + "num_" + numOfBit + "_bit" + "_for_csv_line_" + lineCounter + " = this->connection()->upflow()->data_map[item_id_from_csv_line_" + lineCounter + "][0].first;";
                break;

            case "FLOAT32": // as int for now
                numOfFloat++;
                returnString = "    int " + "num_" + numOfFloat + "_float" + "_for_csv_line_" + lineCounter + " = std::stoi(this->connection()->upflow()->data_map[item_id_from_csv_line_" + lineCounter + "][0].first);";
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
        String scriptRecordFilename = "../ResiGate/scripts/base/protocols/mms/auto_events_record.bro";

        // run csv 
        String configurationFile = "./mms_configuration.csv";
        CsvGenerator.ParseDOM(configurationFile);

        String fileName = "user_configuration_test.csv";

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

            // write scripts records
            FileWriter scriptRecordWriter = new FileWriter(scriptRecordFilename);
            PrintWriter scriptRecordOutputStream = new PrintWriter(scriptRecordWriter);
            scriptRecordOutputStream.println("module mms_test;");
            scriptRecordOutputStream.println();
            scriptRecordOutputStream.println("export {");
            scriptRecordOutputStream.println("  type Info: record {");
            scriptRecordOutputStream.println("      ## Timestamp for when the event happened.");
            scriptRecordOutputStream.println("      ts:     time    &log;");
            scriptRecordOutputStream.println("      ## Unique ID for the connection.");
            scriptRecordOutputStream.println("      uid:    string  &log;");
            scriptRecordOutputStream.println("      ## The connection's 4-tuple of endpoint addresses/ports.");
            scriptRecordOutputStream.println("      id:     conn_id &log;");
            scriptRecordOutputStream.println();
            scriptRecordOutputStream.println("      # ## TODO: Add other fields here that you'd like to log.");
            scriptRecordOutputStream.println("      Message_type: string &log;");
            scriptRecordOutputStream.println("      invoke_id: int &log;");

            // write scripts
            FileWriter scriptWriter = new FileWriter(scriptFilename);
            PrintWriter scriptOutputStream = new PrintWriter(scriptWriter);
            scriptOutputStream.println("module mms_test;");
            scriptOutputStream.println();
            scriptOutputStream.println("redef enum Log::ID += { LOG };");
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
                analyzerOutputStream.println("    if(this->connection()->upflow()->data_map.find(\"" + values[0] + "\") != this->connection()->upflow()->data_map.end() && !this->connection()->upflow()->is_request && " + attributeNames[0] + "_from_csv_line_" + lineCounter + ".compare(this->connection()->upflow()->domain_item_id_map[this->connection()->upflow()->invoke_id]) == 0){");
                analyzerOutputStream.println("      cout << \"\\x1B[36m\" << \"[MAP DATA]\" << \"\\033[0m\" << this->connection()->upflow()->data_map[item_id_from_csv_line_" + lineCounter + "][0].first << endl;");
                analyzerOutputStream.println("  " + buildDataString(values[2], lineCounter));
                analyzerOutputStream.println("      BifEvent::generate_auto_event_" + lineCounter + "(connection()->bro_analyzer(),");
                analyzerOutputStream.println("                                      connection()->bro_analyzer()->Conn(),");
                analyzerOutputStream.println("                                      this->connection()->upflow()->invoke_id,");
                analyzerOutputStream.println(buildBifDataString(values[2], lineCounter));
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
                eventOutputStream.print(buildEventDataString(values[2], lineCounter));
                eventOutputStream.print("%);" + "\n");

                // auto_events_record.bro
                scriptRecordOutputStream.println("      " + buildEventDataString(values[2], lineCounter) + " &log;");

                // auto_events.bro
                scriptOutputStream.println("event auto_event_" + lineCounter + "(c: connection, invoke_id: int, " + buildEventDataString(values[2], lineCounter) + ") {");
                scriptOutputStream.println("    local info: Info;");
                scriptOutputStream.println("	info$ts = network_time();");
                scriptOutputStream.println("	info$uid = c$uid;");
                scriptOutputStream.println("	info$id = c$id;");
                scriptOutputStream.println("	info$Message_type = \"auto events\";");
                scriptOutputStream.println("	info$invoke_id = invoke_id;");
                scriptOutputStream.println("	info$" + buildScriptDataString(values[2], lineCounter) + " = " + buildScriptDataString(values[2], lineCounter) + ";");
                scriptOutputStream.println("	Log::write(LOG, info);");
                scriptOutputStream.println("    print \"[SCRIPT EVENT] \" , " + buildScriptDataString(values[2], lineCounter) + " , invoke_id;");
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

            // script record
            scriptRecordOutputStream.println("  };");
            scriptRecordOutputStream.println("}");
            scriptRecordOutputStream.println();
            scriptRecordOutputStream.close();

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
