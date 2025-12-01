package com.example.bhartiye_jangannah.modals

data class CensusEntry(

    var userId : String ?= null,

    var id : String ?= null,

    var name: String ?= null,

    var headOfFamily : String ?= null,

    var fathersName : String ?= null,

    var relationshipWithHead : String?= null,

    var aadharNo : String = "",

    var gender : String ?= null,

    var religion : String ?= null,

    var caste: String ?= null,

    var literacyStatus : String ?= null,

    var lastClassStudied : String ?= null,

    var maritalStatus : String ?= null,

    var ageAtMarriage: String ?= null,

    var motherTongue : String ?= null,

    var otherLanguagesKnown : String ?= null,

    var dateOfBirth : String ?= null,

    var age : String ?= null,

    var occupation : String ?= null,

    var address : String ?= null,

    var district : String ?= null,

    var state : String ?= null,

    var birthmark : String ?= null,

    var disability : String ?= null,

    var typeOfDisability : String ?= null,

    var imageUrl: String ?= null
)
