package chain.fake_authentication.resolve;

import chain.Link;
import com.google.gson.JsonObject;

class LinkCheckJson extends Link<ResolveChain> {
     public LinkCheckJson(ResolveChain chain) {
         super(chain);
     }

     /**
      * resolve the request within this chain
      *
      * @return <code>true</code> when successfully run this block code
      * <code>false</code> when unsuccessfully run this block code
      */
     @Override
     protected boolean resolve() {
         JsonObject body = chain.getProcessObject().get("body").getAsJsonObject();
         //check the client id
         if (!body.has("clientId")) {
             System.err.println("the body does not contain name of the client");
             chain.getProcessObject().addProperty("error", "body does not contain name of the client");
             return false;
         }
         try{
             body.get("clientId").getAsString();
         } catch (Exception e) {
             System.err.println("the client id can not be converted to string");
             e.printStackTrace();
             chain.getProcessObject().addProperty("error", "client id can not be converted to string");
             return false;
         }
         // check the requested module
         if (!body.has("requestModule")) {
             System.err.println("the body does nto contain list of request modules");
             chain.getProcessObject().addProperty("error", "body does not contain list of request modules");
             return false;
         }
         try {
             body.get("requestModule").getAsJsonArray();
         } catch (Exception e){
             System.err.println("the requestModule is not in correct form");
             e.printStackTrace();
             chain.getProcessObject().addProperty("error", "the requestModule is not in correct format");
             return false;
         }
         return true;
     }
 }
