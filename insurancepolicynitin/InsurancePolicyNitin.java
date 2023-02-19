package insurancepolicynitin;

import insurancepolicynitin.Admin.Admin;
import insurancepolicynitin.User.User;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class InsurancePolicyNitin {

  private static int policyId;
  private static boolean adminLogin;
  private static int adminId;
  private static int userId;
  private static boolean adminNewCreated;
  private static boolean userLogin;
  private static boolean userNewCreated;
  public static Scanner sc;

  static {
    policyId = 0;
    userId = 0;
    adminId = 0;
    adminLogin = false;
    adminNewCreated = false;
    userLogin = false;
    userNewCreated = false;
    sc = new Scanner(System.in);
  }

  public static void main(String[] args) {
    File policyFile = new File("Policy.txt"); // polices File
    File userPolicies = new File("policybuy.txt"); // purchased policies file
    File fileAdmin = new File("Admin.txt");
    File fileUser = new File("User.txt");

    try {
      FileWriter policyFileWrite = new FileWriter(policyFile, true);
      policyFileWrite.write("");
      policyFileWrite.close();
      FileWriter adminWrite = new FileWriter(fileAdmin, true);
      adminWrite.write("");
      adminWrite.close();
      FileWriter userWrite = new FileWriter(fileUser, true);
      userWrite.write("");
      userWrite.close();
      FileWriter userPolicyWrite = new FileWriter(userPolicies, true);
      userPolicyWrite.write("");
      userPolicyWrite.close();
    } catch (IOException e) {
      System.out.println(e);
    }
    // variables for inputs
    StringBuilder name = new StringBuilder("");
    StringBuilder password = new StringBuilder("");
    StringBuilder policyType = new StringBuilder("");

    while (true) {
      System.out.println("--------------------------------------------------");
      System.out.println("1 for Admin");
      System.out.println("2 for User");
      System.out.println("-1 for exit");
      System.out.println();

      System.out.println("Choice = ");
      int choice = sc.nextInt();

      // Admin----------------------------------------------
      // admin part is left
      if (choice == 1) {
        while (true) {
          if (adminLogin || adminNewCreated) {
            System.out.println("--------------------------------------------------");
            System.out.println("1 for Adding Policy");
            System.out.println("2 for deleting Policy");
            System.out.println("3 for See All Policies ");
            System.out.println("-1 for LogOut");
            System.out.println();

            System.out.println("Choice = ");
            choice = sc.nextInt();

            if (choice == 1) {
              System.out.println("Enter Policy Type i.e \n C->Car, \n H->House, \n L->Life");
              policyType = new StringBuilder(sc.next());
              System.out.println("Enter Policy Name = ");
              name = new StringBuilder(sc.next());
              System.out.println("Enter Policy Price = ");
              int price = sc.nextInt();
              System.out.println("Enter Policy Year = ");
              StringBuilder policyYear = new StringBuilder(sc.next());
              Admin.insertPolicy(policyType.charAt(0), ++policyId, name, price, policyYear, policyFile);
            } else if (choice == 2) {
              System.out.println("Enter Policy Id = ");
              choice = sc.nextInt();
              Admin.deletePolicy(choice, policyFile);
            } else if (choice == 3) {
              User.seeAllPolicies(policyFile);
            } else if (choice == -1) {
              adminLogin = adminNewCreated = false;
              break;
            } else {
              System.out.println("Wrong Option, Try Again!!");
            }
          } else {
            System.out.println("--------------------------------------------------");
            System.out.println("1 for New Account For Admin");
            System.out.println("2 for Login");
            System.out.println("-1 for Back");
            System.out.println();
            System.out.println();

            System.out.println("Choice = ");
            choice = sc.nextInt();

            if (choice == 1) {
              System.out.println("Name = ");
              name = new StringBuilder(sc.next());
              System.out.println("Password = ");
              password = new StringBuilder(sc.next());
              Admin admin = new Admin(name, ++adminId, password);
              adminNewCreated = admin.insertAdmin(fileAdmin);
            } else if (choice == 2) {
              System.out.println("Name = ");
              name = new StringBuilder(sc.next());
              System.out.println("Password = ");
              password = new StringBuilder(sc.next());
              adminLogin = Admin.adminLogin(name, password, fileAdmin);
              if (adminLogin) {
                System.out.println("User Found:) in Database");
              } else {
                System.out.println("Admin not Found");
              }
            } else if (choice == -1) {
              break;
            } else {
              System.out.println("Wrong Option, Try Again!!");
            }
          }
        }
      } // User-----------------------------------------
      else if (choice == 2) {
        while (true) {
          if (userLogin || userNewCreated) {
            User.checkForExpiryOfInsurance(name, userPolicies);// check for expiry of user policy

            System.out.println("--------------------------------------------------");
            System.out.println("1 for Policies");
            System.out.println("2 for Purchased Policies");
            System.out.println("-1 for LogOut");
            System.out.println();
            System.out.println("Choice = ");
            choice = sc.nextInt();

            if (choice == 1) {
              while (true) {
                System.out.println();
                System.out.println("1 for Car Insurance Policy");
                System.out.println("2 for House Insurance Policy");
                System.out.println("3 for Life Insurance Policy");
                System.out.println("4 for See All Policies ");
                System.out.println("-1 for back");
                System.out.println();

                System.out.println("Choice = ");
                choice = sc.nextInt();

                if (choice == 1) {
                  insertTheSpecificPolicyType(choice, policyFile, userPolicies, name,
                      User.showTypesOfPolicy('C', policyFile));
                } else if (choice == 2) {
                  insertTheSpecificPolicyType(choice, policyFile, userPolicies, name,
                      User.showTypesOfPolicy('H', policyFile));
                } else if (choice == 3) {
                  insertTheSpecificPolicyType(choice, policyFile, userPolicies, name,
                      User.showTypesOfPolicy('L', policyFile));
                } else if (choice == 4) {
                  User.seeAllPolicies(policyFile);
                  insertTheSpecificPolicyType(choice, policyFile, userPolicies, name,
                      User.showTypesOfPolicy('A', policyFile));
                } else if (choice == -1) {
                  break;
                } else {
                  System.out.println("Wrong Option");
                }
              }

            } else if (choice == 2) {
              System.out.println(name);
              User.seeAllBuyedPolices(userPolicies, name);
              System.out.println();
            } else if (choice == -1) {
              userLogin = userNewCreated = false;
              break;
            } else {
              System.out.println("Wrong Option, Try Again!!");
            }
          } else {
            System.out.println("--------------------------------------------------");
            System.out.println("1 for New User");
            System.out.println("2 for Login");
            System.out.println("-1 for back");
            System.out.println();
            System.out.println("Choice = ");
            choice = sc.nextInt();

            if (choice == 1) {
              System.out.println("Name = ");
              name = new StringBuilder(sc.next());
              System.out.println("Password = ");
              password = new StringBuilder(sc.next());
              User user = new User(name, ++userId, password);
              userNewCreated = user.insertUser(fileUser);

            } else if (choice == 2) {
              System.out.println("Name = ");
              name = new StringBuilder(sc.next());
              System.out.println("Password = ");
              password = new StringBuilder(sc.next());
              userLogin = User.userLogin(name, password, fileUser);
              if (adminLogin) {
                System.out.println("User Found:) in Database");
              } else {
                System.out.println("Wrong Entry :( Try Again");
              }
            } else if (choice == -1) {
              break;
            } else {
              System.out.println("Wrong Option");
            }
          }
        }
      } else if (choice == -1) {
        System.out.println("--------------------------------------------------");
        System.out.println("Thanks For Using Nitin Insurance Application :)");
        break;
      } else {
        System.out.println("Wrong Option, Try Again!!");
      }
    }
    sc.close();
  }

  private static void insertTheSpecificPolicyType(int choice, File policyFile, File userPolicies, StringBuilder name,
      ArrayList id) {
    System.out.println("Select Polices from above");
    choice = sc.nextInt();
    if (id.contains(choice)) {
      User.insertBuyedPolicy(choice, policyFile, userPolicies, name);
      System.out.println("Policy No. " + choice + " Selected");
    } else {
      System.out.println("No Policy Found :(");
    }
  }

}
