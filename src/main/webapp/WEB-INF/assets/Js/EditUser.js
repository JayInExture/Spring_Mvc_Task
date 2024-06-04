$(document).ready(function() {

/*
var addressIndex = document.getElementById("address-index").value;

      $('#add-address').click(function() {
     var addressCount = $('.address-field').length; // Count the existing address fields
        const newAddressField = `
            <div class="address-field">
                <input id="address-index-${addressCount}" name="addresses[${addressCount}].index" value="${addressCount}" readonly>
                         <input type="text" name="addresses[${addressIndex}].street" placeholder="Street" required>
                         <label for="city">City:</label>
                         <input type="text" name="addresses[${addressCount}].city" placeholder="City" required>
                         <label for="zip">ZIP:</label>
                         <input type="text" name="addresses[${addressCount}].zip" placeholder="Zip" required>
                         <label for="state">State:</label>
                         <input type="text" name="addresses[${addressCount}].state" placeholder="State" required>
                         <button type="button" class="delete-address">Delete Address</button>
                         <br>
                     </div>`;
        $('#address-fields').append(newAddressField);
        updateDeleteButtons();
      });
       $(document).on("click", ".delete-address", function() {
          if ($(".address-field").length === 1) {
                      alert("At least one address component must remain.");
                      return;
                  }
             $(this).closest('.address-field').remove();
             updateDeleteButtons();
           });
            function updateDeleteButtons() {
                $(".delete-address").toggle($(".address-field").length > 1);
                }
            updateDeleteButtons();*/

    });
//               let imageCount = $('#image-fields .image-field').length;
//
//                  $('#add-image').click(function() {
//                      imageCount++;
//                      const newImageField = `
//                          <div class="image-field">
//                              <input type="file" name="imageFiles" accept="image/*">
//                              <img class="preview-image" src="" alt="Preview Image">
//                              <button type="button" class="delete-image">Delete Image</button>
//                              <br>
//                          </div>`;
//                      $('#image-fields').append(newImageField);
//                      updateImageDeleteButtons();
//                  });
//
//                  $(document).on("click", ".delete-image", function() {
//                      if ($(".image-field").length === 1) {
//                          alert("At least one image must remain.");
//                          return;
//                      }
//                      $(this).closest('.image-field').remove();
//                      updateImageDeleteButtons();
//                  });
//
//                  function updateImageDeleteButtons() {
//                      $(".delete-image").toggle($(".image-field").length > 1);
//                  }
//
//                  $(document).on("change", "input[type=file]", function() {
//                      var input = this; // Store a reference to the input element
//                      if (input.files && input.files[0]) {
//                          var reader = new FileReader();
//                          reader.onload = function(e) {
//                              $(input).siblings(".preview-image").attr("src", e.target.result);
//                          }
//                          reader.readAsDataURL(input.files[0]);
//                      }
//                  });
//
//                  updateImageDeleteButtons();
