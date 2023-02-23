package insurancepolicynitin.companyLogic;

public class BusinessLogic {
  // bussiness logic -----------------------------------------------------
  private static char insurancePassChar;
  public BusinessLogic(char userTypesCharacter){
    insurancePassChar = userTypesCharacter;
  }

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
