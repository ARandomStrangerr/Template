package chain.fake_authentication.init;

import chain.Link;
import memorable.FakeAuthentication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;

public class ReadFile extends Link<InitChain> {
    public ReadFile(InitChain chain) {
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
        /*
        this read the file which contains the authentication info then store it into a hash table for later retreat
         */
        BufferedReader fileReader;
        String[] macAndPrivilege, privilegeList;
        Hashtable<String, HashSet<String>> privilegeTable;
        privilegeTable = new Hashtable<>();
        try {
            fileReader = new BufferedReader(new FileReader(chain.authenticationFile));
            for (String line = fileReader.readLine(); line != null; line = fileReader.readLine()) {
                macAndPrivilege = line.split("=");
                privilegeList = macAndPrivilege[1].split(":");
                HashSet<String> privilegeSet = new HashSet<>(Arrays.asList(privilegeList));
                privilegeTable.put(macAndPrivilege[0], privilegeSet);
            }
        } catch (IOException e) {
            return false;
        }
        FakeAuthentication.getInstance().setPrivilegeTable(privilegeTable);
        return true;
    }
}
