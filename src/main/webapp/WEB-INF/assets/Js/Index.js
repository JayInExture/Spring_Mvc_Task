$(document).ready(function() {
    let addressIndex = 1;

    $("#add-address").click(function() {
        var newAddress = $(".address-field:first").clone();
        newAddress.find("input").val("");
        newAddress.find("input").each(function() {
            var name = $(this).attr("name");
            var newName = name.replace(/\[\d+\]/, "[" + addressIndex + "]");
            $(this).attr("name", newName);
        });
        addressIndex++;
        newAddress.insertAfter(".address-field:last");
        updateDeleteButtons();
    });

    $(document).on("click", ".delete-address", function() {
        if ($(".address-field").length === 1) {
            alert("At least one address component must remain.");
            return;
        }
        $(this).closest(".address-field").remove();
        updateDeleteButtons();
    });

    function updateDeleteButtons() {
        $(".delete-address").toggle($(".address-field").length > 1);
    }
    updateDeleteButtons();


     Dropzone.autoDiscover = false;
            var myDropzone = new Dropzone(".dropzone", {
               url: " ",
               autoProcessQueue: true,
                uploadMultiple: true,
               parallelUploads: 20,
                maxFiles: 50,
               maxThumbnailFilesize:50,
                addRemoveLinks: true,
               acceptedFiles: ".jpg, .jpeg, .png",
               dictDefaultMessage: "Drop your files here!",
               thumbnailWidth: null,
               thumbnailHeight:null,
               resizeWidth: null,
                resizeHeight:null,

           });

            document.getElementById("submit_button").addEventListener("click", function() {
               var images = [];
               $(".dropzone .dz-preview .dz-image img").each(function() {
                   var imageSrc = $(this).attr("src");
                   images.push(imageSrc);
               });
                var form = $("#registration_form"); // Assuming your form has an ID of "registration_form"
                $.each(images, function(index, imageSrc) {
					
					            var hiddenInput = $('<input type="hidden" name="images[' + index + ']" value="' + imageSrc + '">');

                    /*var hiddenInput = $('<input type="hidden" name="images" value="' + imageSrc + '">');*/
                    form.append(hiddenInput);
                });
            });



});
