package chain.incoming_connection.resolve.listener;

import chain.Link;
import com.google.gson.JsonArray;

/**
 * depends on the name of the job needed, change it into array of modules needed to process the request.
 */
class LinkJobTranslate extends Link<ResolveChain> {
     LinkJobTranslate(ResolveChain chain) {
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
         String requestJob = chain.getProcessObject().remove("job").getAsString();
         JsonArray toArray = new JsonArray();
         switch (requestJob){
             case "GetInvoice":
                 toArray.add("ViettelInvoiceGet");
                 break;
         }
         chain.getProcessObject().add("to", toArray);
         return true;
     }
 }
