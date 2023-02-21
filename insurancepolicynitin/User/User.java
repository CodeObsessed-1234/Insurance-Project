package insurancepolicynitin.User;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import insurancepolicynitin.companyLogic.BusinessLogic;;

public class User extends BusinessLogic {

  protected StringBuilder name;
  protected static Date date;
  protected StringBuilder pass;
  protected String policyChoice;
  protected static int month;

  public User(StringBuilder name, StringBuilder pass) {
    super('~');
    this.name = name;
    this.pass = pass;
  }

  public boolean insertUser(File file) {
    try {
      if (userLogin(name, pass, file)) {
        System.out.println("Already a Registered in DataBase! Try Again :)");
        return false;
      } else {
        FileWriter UserWrite = new FileWriter(file, true);
        pass = encryptPass(pass);
        UserWrite.write("N@" + name + " " + "P!" + pass + " \n");
        UserWrite.close();
        System.out.println("Insertion SuccesFull:)\n");
        return true;
      }
    } catch (IOException e) {
      System.out.println("Error in Data Entry\n");
    }
    return false;
  }

  public static boolean userLogin(StringBuilder name, StringBuilder pass, File file) {

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

  public static void seeAllPolicies(File file) {
    try {
      BufferedReader policyUserFetch = new BufferedReader(new FileReader(file));
      String line;
      String formatedData;
      int len = 0;
      while ((line = policyUserFetch.readLine()) != null) {
        formatedData = "";
        formatedData = formattedPoliciesByType(line, formatedData);
        System.out.println(formatedData);
        ++len;
      }

      if (len == 0) {
        System.out.println("Please Contact Admin To Add A Policy In Database :(");
      }
      policyUserFetch.close();
    } catch (IOException e) {
      System.out.println(e);
    }
  }

  public static void seeAllBuyedPolices(File file, StringBuilder name) {
    try {
      BufferedReader br = new BufferedReader(new FileReader(file));
      String data;
      String copyName = "";
      int len = 0;
      String stripingStringDataOfPolicy = "";
      while ((data = br.readLine()) != null) {
        stripingStringDataOfPolicy = "";
        ++len;
        // find the user policy from boughtPolicy File
        copyName = "";
        for (int i = 2; i < data.length(); i++) {
          if (data.charAt(i - 1) == '!') {
            while (data.charAt(i) != ' ') {
              copyName += data.charAt(i);
              i++;
            }
          }
          if (data.charAt(i) == '!') {
            continue;
          }
          if (data.charAt(i) == '@') {
            continue;
          }
          if (data.charAt(i) == '#') {
            continue;
          } else {
            stripingStringDataOfPolicy += data.charAt(i);
          }

        }
        System.out.println(len+" is len");
        if (copyName.equals(name.toString()) && len > 0) {
          System.out.println(stripingStringDataOfPolicy);
        } else {
          System.out.println("Not Found any Record in Database");
        }
      }
      br.close();
    } catch (IOException e) {
      System.out.println(e);
    }
  }

  public static String insertBuyedPolicy(int id, File file, File file1, StringBuilder name) {
    try {
      BufferedReader br = new BufferedReader(new FileReader(file));
      String data;
      int actualId = 0;
      while ((data = br.readLine()) != null) {
        actualId = 0;
        for (int i = 2; i < data.length(); i++) {
          if (data.charAt(i) == '@') {
            break;
          }
          actualId = actualId * 10 + (int) (data.charAt(i)) - 48;
        }
        if (actualId == id) {
          break;
        }
      }
      br.close();
      FileWriter boughtPolicy = new FileWriter(file1, true);
      date = new Date();
      int year = date.getYear() + 1900;
      boughtPolicy.write("N!" + name + " " + data + "Y^" + year + " M>" + date.getMonth() + " \n");
      boughtPolicy.close();

      return "Success";
    } catch (Exception e) {
      System.out.println(e);
    }
    return "Failed";
  }

  // Expiry of any Policy Complete then alert will occur
  public static void checkForExpiryOfInsurance(StringBuilder name, File file) {
    try {
      BufferedReader br = new BufferedReader(new FileReader(file));
      String data;
      date = new Date();
      String prevMonth = "";
      String prevYear = "";
      String policyYear = "";
      String stripingStringDataOfPolicy = "";
      while ((data = br.readLine()) != null) {
        policyYear = "";
        prevMonth = "";
        prevYear = "";
        stripingStringDataOfPolicy = "";
        for (int i = 1; i < data.length(); i++) {
          if (data.charAt(i - 1) == '^') {
            while (data.charAt(i) != ' ') {
              prevMonth += data.charAt(i);
              i++;
            }
          }
          if (data.charAt(i) == '!') {
            continue;
          }
          if (data.charAt(i) == '@') {
            continue;
          }
          if (data.charAt(i) == '#') {
            continue;
          }
          if (data.charAt(i) == '%') {
            continue;
          }
          if (data.charAt(i - 1) == '>') {
            while (data.charAt(i) != ' ') {
              prevYear += data.charAt(i);
              i++;
            }
          }
          if (data.charAt(i) == '^') {
            continue;
          }
          if (data.charAt(i - 1) == '%') {
            while (data.charAt(i) != ' ') {
              policyYear += data.charAt(i);
              i++;
            }
            stripingStringDataOfPolicy += policyYear + 'Y';
          } else {
            stripingStringDataOfPolicy += data.charAt(i);
          }
        }
        if (Integer.parseInt(prevMonth) + Integer.parseInt(policyYear) + Integer.parseInt(prevYear) == date.getMonth()
            + date.getYear() + 1900) {
          System.out.println("Your Policy, " + stripingStringDataOfPolicy + " is Expiring Soon ");
        }
        if (Integer.parseInt(prevMonth) + Integer.parseInt(policyYear) + Integer.parseInt(prevYear) < date.getMonth()
            + date.getYear() + 1900) {
          System.out.println("Your Policy, " + stripingStringDataOfPolicy + " is Expired. \nPlease reniew the Policy.");
        }
      }
      br.close();
    } catch (IOException e) {
      System.out.println(e);
    }
  }

  // show the policy by its types
  public static ArrayList showTypesOfPolicy(char policyType, File file) {
    try {
      BufferedReader br = new BufferedReader(new FileReader(file));
      String data;
      int actualId = 0;
      ArrayList<Integer> idListPolicy = new ArrayList<>();
      String stripingStringDataOfPolicy = "";
      while ((data = br.readLine()) != null) {
        stripingStringDataOfPolicy = "";
        if (policyType == 'A') {
          actualId = 0;
          for (int i = 2; i < data.length(); i++) {
            if (data.charAt(i) == '@') {
              break;
            }
            actualId = actualId * 10 + (int) (data.charAt(i)) - 48;
          }
          idListPolicy.add(actualId);
        }
        if (data.charAt(0) == policyType) {
          stripingStringDataOfPolicy = formattedPoliciesByType(data, stripingStringDataOfPolicy);
          actualId = 0;
          for (int i = 2; i < data.length(); i++) {
            if (data.charAt(i) == '@') {
              break;
            }
            actualId = actualId * 10 + (int) (data.charAt(i)) - 48;
          }
          idListPolicy.add(actualId);
          System.out.println(stripingStringDataOfPolicy);
        }
      }
      br.close();
      return idListPolicy;
    } catch (IOException e) {
      System.out.println(e);
    }
    return new ArrayList<>(0);
  }

  public static String formattedPoliciesByType(String data, String stripingStringDataOfPolicy) {
    System.out.println("C -> Car");
    System.out.println("H -> House");
    System.out.println("L -> Life");
    System.out.println("% -> Years");

    for (int i = 0; i < data.length(); i++) {
      if (data.charAt(i) == '@') {
        continue;
      }
      if (data.charAt(i) == '#') {
        continue;
      } else {
        stripingStringDataOfPolicy += data.charAt(i);
      }
    }
    return stripingStringDataOfPolicy;
  }
}
