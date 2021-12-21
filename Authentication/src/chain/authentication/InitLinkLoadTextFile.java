package chain.authentication;

import chain.Link;
import memorable.AuthenticationMemorable;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class InitLinkLoadTextFile extends Link<InitChain> {
    public InitLinkLoadTextFile(InitChain chain) {
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
        LinkedHashMap<String, List<String>> table = new LinkedHashMap<>();

        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(chain.getTextFileName()));
        } catch (IOException e){
            e.printStackTrace();
            System.exit(1);
            return false;
        }
        try {
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                String[] nameValue = line.split("="),
                        values = nameValue[1].split(",");
                table.put(nameValue[0], Arrays.asList(values));
            }
        }catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
            return false;
        }

        AuthenticationMemorable.setAuthenticationMap(table);
        return true;
    }
}
