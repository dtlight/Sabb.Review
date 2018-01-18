$(document).ready(function(){
    $("#submitForm").click( function(){

        alert("hiworld!");

        //---SECTION 1 VARS---//
        var name = $("#name");
        var department = $("#department");
        var appointmentDate = $("#appointmentDate");

        //'Contact details during leave'
        var presentPost = $("#presentPost");
        var address = $("#address");
        var email = $("#email");

        //Emergency contact details
        var emergencyName = $("#emergencyContactName");
        var emergencyTel = $("#emergencyTelephoneNo");
        var emergencyAddress  = $("#emergencyAddress");
        var remuneration = $("#remuneration");

        //---SECTION 2 VARS---//
        var termsCount = $("#termsCount");
        var lastSabbaticalDate  = $("#lastSabbaticalDate");
        var lastSabbaticalReport = $("#lastSabbaticalReport");

        //'Last three research outputs'
        var outputOne = $("#researchOutput1");
        var outputTwo = $("#researchOutput2");
        var outputThree = $("#researchOutput3");

        var sabbaticalJustification = $("#leaveJustification");
        var sabbaticalStartDate = $("#leaveStartDate");
        var sabbaticalEndDate  = $("#leaveEndDate");

        var objectives = $("#leaveObjectives");
        var collegeStrategyDesc = $("#collegeStrategicAlign");
        var anticipatedOutputs = $("#anticipatedOutputs");

        var sabbaticalprogram = $("#programmeOutline");
        var grants = $("#appliedGrants");
        var professionalDevelopmentDesc = $("#professionalDevelopmentOutline");

        //TODO - Add signature box code.
    });

});

