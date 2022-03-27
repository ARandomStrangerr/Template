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
         if (!body.has("clientId")) {

             return false;
         }
         return true;
     }
 }
