class AccessData{
  constructor(username, password){
      this._username = username
      this._password = password
  }

  getUsername(){
      return this._username;
  }

  setUsername(username){
    this._username = username;
  }

  getPassword(){
      return this._password;
  }

  setPassword(password)
  {
      this._password = password;
  }
  
  validadeForm(){
      if(''==this._username || ''==this._password){
          throw new 'Dados de acesso inválidos'
      }
  }
}