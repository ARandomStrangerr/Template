package chain.authentication;

import chain.Link;
import com.google.gson.JsonArray;
import memorable.AuthenticationMemorable;

import java.util.List;


public class ProcessLinkGetPrivilege extends Link<ProcessChain> {
    public ProcessLinkGetPrivilege(ProcessChain chain) {
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
        JsonArray jsonArray = new JsonArray();
        try {
            List<String> privilegeList = AuthenticationMemorable
                    .getAuthenticationMap()
                    .get(chain.getProcessObject()
                            .get("body")
                            .getAsJsonObject()
                            .remove("clientIdentity")
                            .getAsString()
                    );

            for (String ele : privilegeList) {
                jsonArray.add(ele);
            }
        } catch (NullPointerException e){
            System.err.println("Access user not found");
        }

        chain.getProcessObject()
                .get("body")
                .getAsJsonObject()
                .add("accessList", jsonArray);

        return true;
    }
}
