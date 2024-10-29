class ChangeData{
    constructor(userCreate, creationDate, userChange, changeDate){
        _userCreate = userCreate, 
        _creationDate = creationDate, 
        _userChange = userChange, 
        _changeDate = changeDate
    }

    getUserCreate(){
        return this.getUserCreate;
    } 

    setUserCreate(userCreate){
        this._userCreate = userCreate;
    }

    getCreationDate(){
        return _creationDate;
    }

    setCreationDate(creationDate){
      this._creationDate = creationDate;
    }

    getUserChange(){
        return this.getUserChange;
    } 

    setUserChange(userChange){
        this._userChange = userChange;
    }

    getChangeDate(){
        return _changeDate;
    }

    setChangeDate(changeDate){
        this._changeDate = changeDate;
    }
}