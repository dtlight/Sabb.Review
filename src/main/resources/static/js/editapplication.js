$(document).ready(function(){
    $(function() {
        $('#navbar').load('navbar.html');
        $('#footer').load('footer.html');

        //Need to load existing fields in here.
    });

    $("#submitForm").submit(function (e) {
        var formSubmit = {
            name: $('#name'),
            department: $('#department'),
            address: $('#address'),

            appointmentDate: $('#appointmentDate'),
            presentPost: $('#presentPost'),
            email: $('#email'),

            emergencyContactName: $('#emergencyContactName'),
            emergencyTelephoneNo: $('#emergencyTelephoneNo'),
            emergencyEmail: $('#emergencyEmail'),

            emergencyAddress: $('#emergencyAddress'),
            remuneration: $('#remuneration'),
            termsCount: $('#termsCount'),

            lastSabbaticalDate: $('#lastSabbaticalDate'),
            sabbaticalObjectives: $('#sabbaticalObjectives'),
            lastSabbaticalReport: $('#lastSabbaticalReport'),

            researchOutput1: $('#researchOutput1'),
            researchOutput2: $('#researchOutput2'),
            researchOutput3: $('#researchOutput3'),

            leaveStartDate: $('#leaveStartDate'),
            leaveEndDate: $('#leaveEndDate'),
            leaveJustification: $('#leaveJustification'),

            leaveObjectives: $('#leaveObjectives'),
            collegeStrategicAlign: $('#collegeStrategicAlign'),
            anticipatedOutputs: $('#anticipatedOutputs'),
            programmeOutline: $('#programmeOutline'),
            appliedGrants: $('#appliedGrants'),
            professionalDevelopmentOutline: $('#professionalDevelopmentOutline')
        };

        var settings = {
            "async": true,
            "crossDomain": true,
            "url": "http://sabb.review/api/fieldinstance/1/value/",
            "method": "PUT",
            "headers": {}
        }
        $.ajax(settings).done(function (response) {
            console.log(response);
        });
    });

});

