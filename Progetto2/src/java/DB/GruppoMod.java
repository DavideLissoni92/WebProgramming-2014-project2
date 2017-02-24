package DB;

public class GruppoMod {

    int id, npost, nutenti;
    String name, avatar;
    boolean chiuso, pubblico;

    //funzione che ritorna il nome di un gruppo
   public boolean getChiuso() {

        return chiuso;

    }
    //funzione che setta il nome di un gruppo

    public void setChiuso(boolean chiuso) {

        this.chiuso = chiuso;

    }   
    
       //funzione che ritorna il nome di un gruppo
   public boolean getPubblico() {

        return pubblico;

    }
    //funzione che setta il nome di un gruppo

    public void setPubblico(boolean pubblico) {

        this.pubblico = pubblico;

    }   
    
    
    
    //funzione che ritorna il nome di un gruppo
   public String getName() {

        return name;

    }
    //funzione che setta il nome di un gruppo

    public void setName(String nome) {

        this.name = nome;

    }

    //funzione che ritorna l'id di un gruppo
    public int getid() {

        return id;

    }

    //funzione che setta l'id di un gruppo
    public void setid(int id) {

        this.id = id;

    }

    //funzione che ritorna la data di creazione di un gruppo
    public String getAvatar() {

        return avatar;

    }

    //funzione che setta la data di creazione di un gruppo
    public void setAvatar(String avatar) {

        this.avatar = avatar;

    }

    //funzione che ritorna l'idadmin di un gruppo
    public int getNpost() {

        return npost;

    }

    //funzione che setta l'idadmin di un gruppo
    public void setNpost(int npost) {

        this.npost = npost;

    }
    
    public int getNutenti() {

        return nutenti;

    }

    //funzione che setta l'idadmin di un gruppo
    public void setNutenti(int nutenti) {

        this.nutenti = nutenti;

    }

}
