package android;

import database.Database;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import java.sql.SQLException;
import java.util.*;

/**
 * Created by Palash on 4/9/2017.
 */
public class Manifest {
    private final Database db;
    private int minSDK;
    private List<Permission> permissions;
    private int targetSDK;
    private int maxSDK;
    private int id;


    public Manifest() {
        this.permissions = new ArrayList<>();
        db = Database.getInstance();
    }

    public int getMinSDK() {
        return minSDK;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public int getMaxSDK() {
        return maxSDK;
    }

    public void scan(Document doc, int id) {
        // SDK
        NodeList usesSDK = doc.getDocumentElement().getElementsByTagName("uses-sdk");
        NamedNodeMap sdkMap;
        try {
            sdkMap = usesSDK.item(0).getAttributes();
            minSDK = setZeroIfNotPresent("android:minSdkVersion", sdkMap);
            maxSDK = setZeroIfNotPresent("android:maxSdkVersion", sdkMap);
            targetSDK = setZeroIfNotPresent("android:targetSdkVersion", sdkMap);
        } catch (NullPointerException npe) {
            minSDK = 0;
            maxSDK = 0;
            targetSDK = 0;
        }
        setId(db.addManifest(id, this));

        // Permissions
        NodeList nodes = (NodeList) doc.getDocumentElement().getElementsByTagName("uses-permission");
        for (int i = 0; i < nodes.getLength(); i++) {
            Permission p = new Permission(nodes.item(i).getAttributes().getNamedItem("android:name").getNodeValue());
            try {
                p.setId(db.addPermission(getId(), p));
                permissions.add(p);
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private int setZeroIfNotPresent(String attr, NamedNodeMap sdkMap) {
        try {
            return Integer.parseInt(sdkMap.getNamedItem(attr).getNodeValue());
        } catch (Exception e) {
            return 0;
        }
    }

    public int getTargetSDK() {
        return targetSDK;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
