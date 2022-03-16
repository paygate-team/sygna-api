package io.sygna.bridge.sample;


//import com.coolbitx.sygna.bridge.API; modified 
import com.coolbitx.sygna.bridge.Crypto;
import com.coolbitx.sygna.bridge.model.Field;
import com.coolbitx.sygna.config.BridgeConfig;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


public class TestAPI {
	
	public static final String API_KEY = "f41f97f9c32363f6561474a9552e9f2ab110968277dc23068deb72de7a9208ec";
    public static final String PRIVATE_KEY = "10e82b8299a8c2af32df9068d7beca5bbc876d9799ee6f04c1417d70b154989b";
    public static final String PUBLIC_KEY = "0461bfc8639ac65785dfaf287daa31dd3f7535a8afc2cb98d577b4b1fc07e1385bbdf0d4ec7f4b57371d7d45c4282dc3e67482f4c7c4452494aab568dee212450a";
    
    public static void main(String args[]) throws Exception {
    	API api = new API(API_KEY,BridgeConfig.SYGNA_BRIDGE_API_TEST_DOMAIN);
    	
        String privateInfo ="abcd";

        String originatorVASPCode = "VASPUSNY1";

        JsonObject originatorAddrExtraInfo = new JsonObject();
        originatorAddrExtraInfo.addProperty("tag", "123");

        JsonArray originatorAddrExtraInfoArray = new JsonArray();
        originatorAddrExtraInfoArray.add(originatorAddrExtraInfo);

        JsonObject originatorAddr = new JsonObject();
        originatorAddr.addProperty(Field.ADDRESS, "r3kmLJN5D28dHuH8vZNUZpMC43pEHpaocV");
        originatorAddr.add(Field.ADDR_EXTRA_INFO, originatorAddrExtraInfoArray);

        JsonArray originatorAddrs = new JsonArray();
        originatorAddrs.add(originatorAddr);

        String beneficiaryVASPCode = "VASPUSNY2";

        JsonObject beneficiaryAddr = new JsonObject();
        beneficiaryAddr.addProperty(Field.ADDRESS, "rAPERVgXZavGgiGv6xBgtiZurirW2yAmY");

        JsonArray beneficiaryAddrs = new JsonArray();
        beneficiaryAddrs.add(beneficiaryAddr);

        String currencyId = "sygna:0x80000090";
        String amount = "12.5";

        String dataDt = "2020-07-13T05:56:53.088Z";
        String callbackUrl = "https://7434116d30db72c01911efd735cfefdc.m.pipedream.net";
        
        JsonObject originatorVASP = new JsonObject();
        originatorVASP.addProperty(Field.VASP_CODE, originatorVASPCode);
        originatorVASP.add(Field.ADDRS, originatorAddrs);

        JsonObject beneficiaryVASP = new JsonObject();
        beneficiaryVASP.addProperty(Field.VASP_CODE, beneficiaryVASPCode);
        beneficiaryVASP.add(Field.ADDRS, beneficiaryAddrs);

        JsonObject transaction = new JsonObject();
        transaction.add(Field.ORIGINATOR_VASP, originatorVASP);
        transaction.add(Field.BENEFICIARY_VASP, beneficiaryVASP);
        transaction.addProperty(Field.CURRENCY_ID, currencyId);
        transaction.addProperty(Field.AMOUNT, amount);

        JsonObject permissionRequestData = new JsonObject();
        permissionRequestData.addProperty(Field.PRIVATE_INFO, privateInfo);
        permissionRequestData.add(Field.TRANSACTION, transaction);
        permissionRequestData.addProperty(Field.DATA_DT, dataDt);

        JsonObject signedPermissionRequestData = Crypto.signPermissionRequest(permissionRequestData, PRIVATE_KEY);

        JsonObject callbackData = new JsonObject();
        callbackData.addProperty(Field.CALLBACK_URL, callbackUrl);

        JsonObject signedCallbackData = Crypto.signCallBack(callbackData, PRIVATE_KEY);

        JsonObject body = new JsonObject();
        body.add(Field.DATA, signedPermissionRequestData);
        body.add(Field.CALLBACK, signedCallbackData);

        JsonObject status = api.postPermissionRequest(body);

        System.out.printf("status %s\n", status);
    }
}
