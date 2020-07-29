import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

class EventGenerator {

    public static String getBifDataString(String attributeName, String dataType, int lineCounter){
        String returnString = "";
        switch (dataType) {
            case "bool":
                returnString = "                                      " + attributeName + "_from_csv_line_" + lineCounter;
                break;
        
            case "integer":
                returnString = "                                      " + attributeName + "_from_csv_line_" + lineCounter;
                break;
        
            case "utc_time":
                returnString = "                                      string_to_val(" + attributeName + "_from_csv_line_" + lineCounter + ")";
                break;
        
            case "octet_string":
                returnString = "                                      string_to_val(" + attributeName + "_from_csv_line_" + lineCounter + ")";
                break;
        
            case "unsigned_int":
                returnString = "                                      " + attributeName + "_from_csv_line_" + lineCounter;
                break;
        
            case "visiable_string":
                returnString = "                                      string_to_val(" + attributeName + "_from_csv_line_" + lineCounter + ")";
                break;
        
            case "bit_string":
                returnString = "                                      string_to_val(" + attributeName + "_from_csv_line_" + lineCounter + ")";
                break;
        
            case "float":
                returnString = "                                      " + attributeName + "_from_csv_line_" + lineCounter;
                break;
        
            default:
                returnString = "                                      -1";
                break;
        }
        return returnString;
    }

    public static String getEventDataString(String attributeName, String dataType, int lineCounter){
        String returnString = "";
        switch (dataType) {
            case "bool":
                returnString = attributeName + "_from_csv_line_" + lineCounter + ": " + "bool";
                break;
        
            case "integer":
                returnString = attributeName + "_from_csv_line_" + lineCounter + ": " + "int";
                break;
        
            case "utc_time":
                returnString = attributeName + "_from_csv_line_" + lineCounter + ": " + "string";
                break;
        
            case "octet_integer":
                returnString = attributeName + "_from_csv_line_" + lineCounter + ": " + "stirng";
                break;
        
            case "unsigned_int":
                returnString = attributeName + "_from_csv_line_" + lineCounter + ": " + "count";
                break;
        
            case "visible_string":
                returnString = attributeName + "_from_csv_line_" + lineCounter + ": " + "string";
                break;
        
            case "bit_string":
                returnString = attributeName + "_from_csv_line_" + lineCounter + ": " + "string";
                break;
        
            case "float": // FIXME as in for now
                returnString = attributeName + "_from_csv_line_" + lineCounter + ": " + "int";
                break;
        
            default:
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
            analyzerOutputStream.println("  function rule_function(itemID: string, index: int):bool");
            analyzerOutputStream.println("  %{");

            // write event
            FileWriter eventWriter = new FileWriter(eventFilename);
            PrintWriter eventOutputStream = new PrintWriter(eventWriter);

            // write scripts
            FileWriter scriptWriter = new FileWriter(scriptFilename);
            PrintWriter scriptOutputStream = new PrintWriter(scriptWriter);
            scriptOutputStream.println("module Mms;");
            scriptOutputStream.println("");
            scriptOutputStream.println("@load ./main");

            // attributes (file first line)
            String attributeNames[] = inputStream.nextLine().split(",");

            // zeek target data
            int lineCounter = 1;
            while (inputStream.hasNext()) {
                String data = inputStream.nextLine();
                String[] values = data.split(",");

                // analyzer
                for (int i = 0; i < attributeNames.length; i++) {
                    analyzerOutputStream.println("    " + "string " + attributeNames[i] + "_from_csv_line_" + lineCounter + " = \"" + values[i] + "\";");
                }
                analyzerOutputStream.println("    if(data_map.find(\"" + values[0] + "\") != data_map.end()){");
                analyzerOutputStream.println("      cout << \"\\x1B[36m\" << \"[MAP DATA]\" << \"\\033[0m\" << this->connection()->upflow()->data_map[item_id_from_csv_line_" + lineCounter + "][0].first << endl;");
                analyzerOutputStream.println("    }");
                analyzerOutputStream.println("    BifEvent::generate_auto_event_" + lineCounter + "(connection()->bro_analyzer(),");
                analyzerOutputStream.println("                                      connection()->bro_analyzer()->Conn(),");
                for (int i = 0; i < attributeNames.length; i++) {
                    analyzerOutputStream.print("                                      string_to_val(" + attributeNames[i] + "_from_csv_line_" + lineCounter + ")");
                    if(i < attributeNames.length - 1){
                        analyzerOutputStream.print(",\n");
                    } else {
                        analyzerOutputStream.print("\n");
                    }
                }
                analyzerOutputStream.println("                                      );");

                // event_test.bif TODO other data type
                eventOutputStream.println("##");
                eventOutputStream.println("##");
                eventOutputStream.println("##");
                eventOutputStream.println("##");
                eventOutputStream.println("##");
                eventOutputStream.print("event auto_event_"+ lineCounter + "%(c: connection, ");
                for (int i = 0; i < attributeNames.length; i++) {
                    eventOutputStream.print(attributeNames[i] + "_from_csv_line_" + lineCounter + ": " + "string");
                    if (i < attributeNames.length - 1) {
                        eventOutputStream.print(", ");
                    }
                }
                eventOutputStream.print("%);" + "\n\n\n");

                // auto_events.bro
                // scriptOutputStream.println("event read_response(c: connection, invokeID: count, itemID: string, boolean_result: bool) {");
                // scriptOutputStream.println("    local info: Info;");
                // scriptOutputStream.println("	info$ts = network_time();");
                // scriptOutputStream.println("	info$uid = c$uid;");
                // scriptOutputStream.println("	info$id = c$id;");
                // scriptOutputStream.println("	info$Message_type = \"auto events\";");
                // scriptOutputStream.println("	# info$is_orig = is_orig;");
                // scriptOutputStream.println("	info$invokeID = invokeID;");
                // scriptOutputStream.println("	info$itemID = itemID;");
                // scriptOutputStream.println("	info$boolean_result = boolean_result;");
                // scriptOutputStream.println("	if(itemID == target_read_request_item_ID){");
                // scriptOutputStream.println("		# NOTE flip orig and dest table");
                // scriptOutputStream.println("		if(c$id$orig_h != white_destination[1] || c$id$resp_h != white_origin[2]){");
                // scriptOutputStream.println("			# NOTICE");
                // scriptOutputStream.println("		} else {");
                // scriptOutputStream.println("			SCADA_Q2C_In_Sync = boolean_result;");
                // scriptOutputStream.println("			Log::write(Mms::LOG, info);");
                // scriptOutputStream.println("		}");
                // scriptOutputStream.println("	}");
                // scriptOutputStream.println("}");
                lineCounter++;
            }

            // analyzer
            analyzerOutputStream.println("    int attr_value_string = 1;");
            analyzerOutputStream.println("    int attr_value = 1;");
            analyzerOutputStream.println("    BifEvent::generate_mms_event_test(connection()->bro_analyzer(),");
            analyzerOutputStream.println("                                      connection()->bro_analyzer()->Conn(),");
            analyzerOutputStream.println("                                      attr_value_string,");
            analyzerOutputStream.println("                                      attr_value);");
            analyzerOutputStream.println("    return false;");
            analyzerOutputStream.println("  %}");
            analyzerOutputStream.println("}");
            analyzerOutputStream.close();

            // event
            eventOutputStream.println("##");
            eventOutputStream.println("##");
            eventOutputStream.println("##");
            eventOutputStream.println("##");
            eventOutputStream.println("##");
            eventOutputStream.println("event mms_event_test%(c: connection, itemID: int, res: int%);");
            eventOutputStream.close();

            // script
            scriptOutputStream.println("event mms_event_test(c: connection, first_dummay: int, second_dummy: int) {");
            scriptOutputStream.println("	print \"HAHAHAHHA\";");
            scriptOutputStream.println("}");
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
