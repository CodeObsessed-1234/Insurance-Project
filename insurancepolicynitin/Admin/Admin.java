package insurancepolicynitin.Admin;

import java.io.*;
import java.util.ArrayList;

public class Admin extends PassLogicAdmin {

    protected StringBuilder name;
    protected int id;
    protected StringBuilder pass;

    public Admin(StringBuilder name, int id, StringBuilder pass) {
        super();
        this.name = name;
        this.id = id;
        this.pass = pass;
    }

    //adding new admin -------------------------------------------------
    public boolean insertAdmin(File file) {
        try {
            if (adminLogin(name, pass, file)) {
                System.out.println("Already a Registered in DataBase! Try Again :)");
                return false;
            } else {
                FileWriter adminWriter = new FileWriter(file, true);
                pass = encryptPass(pass);
                adminWriter.write("N@" + name + " " + "I#" + id + " " + "P!" + pass + " \n");
                adminWriter.close();
                System.out.println("Insertion Success :)");
                return true;
            }
        } catch (IOException e) {
            System.out.println("Error in Data Entry\n");
        }
        return false;
    }

    //insert the policy by admin----------------------------------------
    public static String insertPolicy(char policyType, int policyId, StringBuilder policyName, int price, StringBuilder validationOfPolicy, File file) {
        try {
            FileWriter adminPolicyWriter = new FileWriter(file, true);
            adminPolicyWriter.write(policyType + " " + policyId + "@ " + policyName + " " + price + "$ %" + validationOfPolicy + " #\n");
            adminPolicyWriter.close();
        } catch (IOException e) {
            return "Error in Data Entry\n";
        }
        return "Success in Policy Insertion";
    }

    //login for admin---------------------------------------
    public static boolean adminLogin(StringBuilder name, StringBuilder pass, File file) {

        StringBuilder copyName = new StringBuilder();
        StringBuilder copyPass = new StringBuilder();
        try {
            BufferedReader adminFileReader = new BufferedReader(new FileReader(file));
            String data;
            int spaceCount = 0;
            while ((data = adminFileReader.readLine()) != null) {
                copyPass.delete(0, copyPass.length());
                copyName.delete(0, copyName.length());
                for (int i = 1; i < data.length(); i++) {
                    if (data.charAt(i - 1) == '@') {
                        spaceCount = i;
                        while (data.charAt(spaceCount) != ' ') {
                            copyName.append(data.charAt(spaceCount));
                            ++spaceCount;
                        }
                    }
                    if (data.charAt(i - 1) == '!') {
                        spaceCount = i;
                        while (data.charAt(spaceCount) != ' ') {
                            copyPass.append(data.charAt(spaceCount));
                            ++spaceCount;
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }

        copyPass = decryptPass(copyPass);
        if (copyName.toString().equals(name.toString()) && copyPass.toString().equals(pass.toString())) {
            return true;
        }
        return false;

    }

    //method to delete a line from file policy.txt
    private static void createNewPolicyFile(File file, ArrayList arr, String dataDeleted) {
        try {
            FileWriter policy = new FileWriter(file, false);
            for (Object i : arr) {
                policy.write(i.toString());
            }
            System.out.println("Deleted " + dataDeleted + " from Database");
            System.out.println();
            policy.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static void deletePolicy(int id, File file) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String data;
            String dataDeleted = "";
            int len = 0;
            ArrayList<String> arr = new ArrayList<String>();
            int actualId = 0;
            while ((data = br.readLine()) != null) {
                actualId = 0;
                ++len;
                for (int i = 2; i < data.length(); i++) {
                    if (data.charAt(i) == '@') {
                        break;
                    }
                    actualId = actualId * 10 + (int) (data.charAt(i)) - 48;
                }
                if (actualId == id) {
                    dataDeleted = data;
                    continue;
                } else {
                    arr.add(data);
                }
            }
            br.close();
            if(len==0){
              System.out.println("No Such Policy ind Database");
              return;
            }
            createNewPolicyFile(file, arr, dataDeleted);
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}

//bussiness logic -----------------------------------------------------
class PassLogicAdmin {

    private static char insurancePassChar = '@';

    protected static StringBuilder encryptPass(StringBuilder pass) {
        for (int i = 0; i < pass.length(); i++) {
            pass.setCharAt(i, (char) (pass.charAt(i) + insurancePassChar));
        }
        return pass;
    }

    protected static StringBuilder decryptPass(StringBuilder pass) {
        for (int i = 0; i < pass.length(); i++) {
            pass.setCharAt(i, (char) (pass.charAt(i) - insurancePassChar));
        }
        return pass;
    }
}
