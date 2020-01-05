package middle_client.utils;

import com.google.gson.*;
import middle_client.utils.RegisterClientModel;

import java.util.ArrayList;
import java.util.Iterator;

public class UserTracking {

    private ArrayList<RegisterClientModel> registeredUsers;
    private ArrayList<String> loggedUsers;

    public UserTracking() {
        registeredUsers = new ArrayList<>();
        loggedUsers = new ArrayList<>();
    }

    public synchronized boolean loginUser(String username) {
        if (!isClientRegistered(username))
            return false;
        return loggedUsers.add(username);
    }

    public synchronized boolean logoutUser(String username) {
        if (!isClientLogged(username))
            return false;
        return loggedUsers.remove(username);
    }

    public Iterator<String> getLoggedUsersIterator() {
        return loggedUsers.iterator();
    }

    public synchronized boolean isClientLogged(String username) {
        return loggedUsers.contains(username);
    }

    public synchronized boolean checkPassword(String password, String username){
        for(int ix=0; ix<registeredUsers.size(); ix++) {
            if (username.equals(registeredUsers.get(ix).getUsername()))
                return password.equals(registeredUsers.get(ix).getPassword());
        }
        return false;
    }

    public synchronized boolean checkUserClear(String username){
        for(int ix=0; ix<registeredUsers.size(); ix++){
            if(username.equals(registeredUsers.get(ix).getUsername()))
                return false;
        }
        return true;
    }

    public synchronized boolean isClientRegistered(String username) {
        Iterator<RegisterClientModel> it = registeredUsers.iterator();
        while (it.hasNext()) {
            RegisterClientModel next = it.next();
            if (username.equals(next.getUsername()))
                return true;
        }
        return false;
    }

    //Esta aqui deve ser usada para adicionar um novo user enquanto o programa corre, sem haver necessidade de reler o ficheiro (no método do protocolo registerUserJson)
    public synchronized boolean addRegisteredUser(RegisterClientModel client){
        if (isClientRegistered(client.getUsername()))
            return false;
        return registeredUsers.add(client);
    }

    //Esta aqui deverá ser chamada no ínicio de cada "sessão" para carregar os que existem no ficheiro, carregando os utilizadores existentes na sua localização apenas
    public void setRegisteredUsers(JsonElement file){
        JsonArray utilizadores
                = (file != null && file.isJsonArray()
                ? file.getAsJsonArray() : new JsonArray());
        if (utilizadores != null) {
            int len = utilizadores.size();
            for (int i=0;i<len;i++){
                JsonObject obj = utilizadores.get(i).getAsJsonObject();
                RegisterClientModel rcm = new RegisterClientModel(obj.get("username").getAsString(), obj.get("password").getAsString());
                registeredUsers.add(rcm);
            }
        }
        System.out.println("\n\nALREADY REGISTERED: " + registeredUsers.toString());
    }

    public Iterator<RegisterClientModel> getAllUsers(){
        return  registeredUsers.iterator();
    }
}