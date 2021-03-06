package com.forgeessentials.auth;

import java.util.HashMap;
import java.util.UUID;

import com.forgeessentials.commons.IReconstructData;
import com.forgeessentials.commons.SaveableObject;
import com.forgeessentials.commons.SaveableObject.Reconstructor;
import com.forgeessentials.commons.SaveableObject.SaveableField;
import com.forgeessentials.commons.SaveableObject.UniqueLoadingKey;
import com.forgeessentials.data.v2.DataManager;

@SaveableObject
public class PlayerPassData {

    private static HashMap<UUID, PlayerPassData> datas = new HashMap<UUID, PlayerPassData>();
    
    @UniqueLoadingKey
    @SaveableField
    public final String username;
    
    @SaveableField
    public String password;

    public PlayerPassData(UUID username, String password)
    {
        this.username = username.toString();
        this.password = password;
    }

    /**
     * Returns the PlayerPassData if it exists.
     *
     * @param username
     * @return
     */
    public static PlayerPassData getData(UUID username)
    {
        PlayerPassData data = datas.get(username);
        if (data == null)
        {
            data = DataManager.getInstance().load(PlayerPassData.class, username.toString());
        }
        return data;
    }

    /**
     * Creates a PlayerPassData
     *
     * @param username
     * @return
     */
    public static void registerData(UUID username, String pass)
    {
        PlayerPassData data = new PlayerPassData(username, pass);
        data.save();
        if (datas.get(data.username) != null)
        {
            datas.put(UUID.fromString(data.username), data);
        }
    }

    /**
     * Discards it.
     * Usually onPlayerLogout
     *
     * @param username
     * @return
     */
    public static void discardData(UUID username)
    {
        PlayerPassData data = datas.remove(username);
        if (data != null)
        {
            data.save();
        }
    }

    /**
     * Completely removes the data.
     *
     * @param username
     * @return
     */
    public static void deleteData(UUID username)
    {
        PlayerPassData data = datas.remove(username);
        DataManager.getInstance().delete(PlayerPassData.class, username.toString());
    }

    @Reconstructor
    private static PlayerPassData reconstruct(IReconstructData data)
    {
        String username = data.getUniqueKey();
        String pass = (String) data.getFieldValue("password");

        return new PlayerPassData(UUID.fromString(username), pass);
    }

    public void save()
    {
        DataManager.getInstance().save(this, username);
    }

}
