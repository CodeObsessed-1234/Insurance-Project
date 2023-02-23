package insurancepolicynitin.Admin;

import insurancepolicynitin.User.User;
import java.io.*;
import java.util.ArrayList;
import insurancepolicynitin.companyLogic.BusinessLogic;;

public class Admin extends BusinessLogic {

  protected StringBuilder name;
  protected StringBuilder pass;

  public Admin(StringBuilder name, StringBuilder pass) {
    super('@');
    this.name = name;
    this.pass = pass;
  }

  // adding new admin -------------------------------------------------
  public boolean insertAdmin(File file) {
    try {
      if (adminLogin(name, pass, file)) {
        System.out.println("Already a Registered in DataBase! Try Again :)");
        return false;
      } else {
        FileWriter adminWriter = new FileWriter(file, true);
        pass = encryptPass(pass);
        adminWriter.write("N@" + name + " " + "P!" + pass + " \n");
        adminWriter.close();
        System.out.println("Insertion Success :)");
        return true;
      }
    } catch (IOException e) {
      System.out.println("Error in Data Entry\n");
    }
    return false;
  }

  // insert the policy by admin----------------------------------------
  public static String insertPolicy(char policyType, int policyId, StringBuilder policyName, int price,
      StringBuilder validationOfPolicy, File file) {

    if (policyType == 'C' || policyType == 'H' || policyType == 'L') {
      try {
        FileWriter adminPolicyWriter = new FileWriter(file, true);
        adminPolicyWriter
            .write(policyType + " " + policyId + "@ " + policyName + " " + price + "$ %" + validationOfPolicy + " #\n");
        adminPolicyWriter.close();
      } catch (IOException e) {
        return "Error in Data Entry\n";
      }

    } else {
      System.out.println("Error Occured because you insert Wrong Info");
      return "Error Occured";
    }

    return "Success in Policy Insertion";
  }

  // login for admin---------------------------------------
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
      adminFileReader.close();
    } catch (IOException e) {
      System.out.println(e);
    }

    copyPass = decryptPass(copyPass);
    if (copyName.toString().equals(name.toString()) && copyPass.toString().equals(pass.toString())) {
      return true;
    }
    return false;

  }

  // logic for maintainance of policy id
  public static int getLastIdOfPolicy(File file) {
    String id = "";
    try {
      BufferedReader br = new BufferedReader(new FileReader(file));
      String data;
      while ((data = br.readLine()) != null) {
        id = "";
        for (int i = 2; i < data.length(); i++) {
          if (data.charAt(i) == '@') {
            break;
          }
          id += data.charAt(i);
        }
      }
      br.close();
    } catch (IOException e) {
      System.out.println(e);
    }
    if (id.length() == 0) {
      return 0;
    } else {
      return Integer.parseInt(id);
    }
  }

  public static void deletePolicy(int id, File file) {
    try {
      BufferedReader br = new BufferedReader(new FileReader(file));
      String data;
      ArrayList<String> dataDeleted = new ArrayList<>();
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
          dataDeleted.add(data);
          continue;
        } else {
          arr.add(data);
        }
      }
      br.close();
      if (len == 0) {
        System.out.println("No Such Policy ind Database");
        return;
      }
      reWriteFile(file, arr, dataDeleted);
    } catch (IOException e) {
      System.out.println(e);
    }
  }

  public static void fetchAllUsers(File file1, File file2) {
    try {
      int len = 0;
      BufferedReader br = new BufferedReader(new FileReader(file1));
      String data;
      while ((data = br.readLine()) != null) {
        ++len;
        String[] arr = formattedUsers(data);
        System.out.println(arr[1]);
        User.seeAllBuyedPolices(file2, new StringBuilder(arr[0]));
        System.out.println("*****************************");
      }
      if (len == 0) {
        System.out.println("Not Found any Record in Database");
      }
      br.close();
    } catch (IOException exception) {
      System.out.println(exception);
    }
  }

  public static void deleteUserDetailsAndPolicy(File userFile, File userPolicyFile, StringBuilder name) {
    try {
      BufferedReader userReader = new BufferedReader(new FileReader(userFile));
      String data = "";
      ArrayList<String> deletedData = new ArrayList<>();
      ArrayList<String> userData = new ArrayList<>();
      while ((data = userReader.readLine()) != null) {
        String[] arr = formattedUsers(data);
        System.out.println(arr[0] + "\n" + name);
        if (arr[0].equals(name.toString())) {
          deletedData.add(data);
          continue;
        }
        userData.add(data);
      }
      System.out.println(deletedData);
      if (deletedData.size() == 0)
        System.out.println("No record found with " + name + " in Database");
      reWriteFile(userFile, userData, deletedData);
      userReader.close();

      ArrayList<String> userPolicyData = new ArrayList<>();
      ArrayList<String> userPolicyDeleted = new ArrayList<>();
      String policyBoughtUserName = "";
      BufferedReader userPolicyReader = new BufferedReader(new FileReader(userPolicyFile));
      while ((data = userPolicyReader.readLine()) != null) {
        for (int i = 2; i < data.length(); i++) {
          if (data.charAt(i) == ' ') {
            break;
          }
          policyBoughtUserName += data.charAt(i);
        }
        if (policyBoughtUserName.equals(name.toString())) {
          userPolicyDeleted.add(data);
        } else {
          userPolicyData.add(data);
        }
      }
      if (userPolicyDeleted.size() == 0)
        System.out.println("No record found with " + name + " in Database");
      reWriteFile(userPolicyFile, userPolicyData, userPolicyDeleted);
      userPolicyReader.close();

    } catch (IOException exception) {
      System.out.println(exception);
    }
  }

  // method to remove data from database or file
  private static void reWriteFile(File file, ArrayList arr, ArrayList dataDeleted) {
    try {
      FileWriter newFile = new FileWriter(file, false);
      for (Object i : arr) {
        newFile.write(i.toString());
      }
      newFile.close();
      for (Object i : dataDeleted) {
        System.out.println("Deleted " + i.toString() + " from Database");
      }
    } catch (IOException exception) {
      System.out.println(exception);
    }
  }

  // return the formatted user details
  private static String[] formattedUsers(String data) {
    String formattedString = "name = ";
    int spaceCount = 0;
    String name = "";
    String[] arr = new String[2];
    for (int i = 2; i < data.length(); i++) {
      if (data.charAt(i) == ' ' && spaceCount == 0) {
        ++spaceCount;
        formattedString += "\npassword = ";
      }
      if (data.charAt(i) != ' ' && spaceCount == 0) {
        name += data.charAt(i);
      }
      formattedString += data.charAt(i);
    }
    arr[0] = name;
    arr[1] = formattedString;
    return arr;
  }
}