$(function () {
//Register user
    $.validator.addMethod("regex", function (value, element, regexp) {
        let re = new RegExp(regexp);
        return this.optional(element) || re.test(value);
    }, "Invalid format");

    $("#userRegister").validate({
        rules: {
            userName: {
                required: true,
                regex: "^[A-Za-z ]{3,30}$"
            },
            emailAddress: {
                required: true,
                regex: "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$"
            },
            mobileNo: {
                required: true,
                regex: "^[6-9]\\d{9}$"
            },
            password: {
                required: true,
                minlength: 8,
                regex:"^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$"
            },
            confirmPassword: {
                required: true,
                equalTo: "#Password1"
            },
            pincode: {
                required: true,
                regex: "^\\d{6}$"
            },
            address: {
                required: true,
                minlength: 5
            },
            city: {
                required: true,
                regex: "^[A-Za-z ]+$"
            },
            state: {
                required: true,
                regex: "^[A-Za-z ]+$"
            },
            file:{
            required:true,
            }
        },
        messages: {
            userName: {
                required: "!! Name Required !!",
                regex: "Only alphabets and spaces allowed"
            },
            emailAddress: {
                required: "!! Email Required !!",
                regex: "Invalid email format"
            },
            mobileNo: {
                required: "!! Mobile No Required !!",
                regex: "Enter a valid 10-digit mobile number starting with 6-9"
            },
            password: {
                required: "!! Password Required !!",
                minlength: "Password must be at least 8 characters",
                regex: "Password must be at least 8 characters and include uppercase, lowercase, number, and special character"
            },
            confirmPassword: {
                required: "!! Confirm Password Required !!",
                equalTo: "Passwords do not match"
            },
            pincode: {
                required: "!! Pincode Required !!",
                regex: "Enter 6 digit pincode"
            },
            address: {
                required: "!! Address Required !!",
                minlength: "Enter full address"
            },
            city: {
                required: "!! City Required !!",
                regex: "Only alphabets allowed"
            },
            state: {
                required: "!! State Required !!",
                regex: "Only alphabets allowed"
            },
            file:{
               required:"!! Image Required !!",
                  }
        },
        errorClass: "text-danger fw-semibold",
        errorElement: "small"
    });
});
$(function () {
//Register Admin
    $.validator.addMethod("regex", function (value, element, regexp) {
        let re = new RegExp(regexp);
        return this.optional(element) || re.test(value);
    }, "Invalid format");

    $("#registerAdmin").validate({
        rules: {
            userName: {
                required: true,
                regex: "^[A-Za-z ]{3,30}$"
            },
            emailAddress: {
                required: true,
                regex: "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$"
            },
            mobileNo: {
                required: true,
                regex: "^[6-9]\\d{9}$"
            },
            password: {
                required: true,
                minlength: 8,
                regex:"^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$"
            },
            confirmPassword: {
                required: true,
                equalTo: "#Password1"
            },
            pincode: {
                required: true,
                regex: "^\\d{6}$"
            },
            address: {
                required: true,
                minlength: 5
            },
            city: {
                required: true,
                regex: "^[A-Za-z ]+$"
            },
            state: {
                required: true,
                regex: "^[A-Za-z ]+$"
            },
            file:{
            required:true,
            }
        },
        messages: {
            userName: {
                required: "!! Name Required !!",
                regex: "Only alphabets and spaces allowed"
            },
            emailAddress: {
                required: "!! Email Required !!",
                regex: "Invalid email format"
            },
            mobileNo: {
                required: "!! Mobile No Required !!",
                regex: "Enter a valid 10-digit mobile number starting with 6-9"
            },
            password: {
                required: "!! Password Required !!",
                minlength: "Password must be at least 8 characters",
                regex: "Password must be at least 8 characters and include uppercase, lowercase, number, and special character"
            },
            confirmPassword: {
                required: "!! Confirm Password Required !!",
                equalTo: "Passwords do not match"
            },
            pincode: {
                required: "!! Pincode Required !!",
                regex: "Enter 6 digit pincode"
            },
            address: {
                required: "!! Address Required !!",
                minlength: "Enter full address"
            },
            city: {
                required: "!! City Required !!",
                regex: "Only alphabets allowed"
            },
            state: {
                required: "!! State Required !!",
                regex: "Only alphabets allowed"
            },
            file:{
               required:"!! Image Required !!",
                  }
        },
        errorClass: "text-danger fw-semibold",
        errorElement: "small"
    });
});

$(function () {
//Reset Password
    $.validator.addMethod("regex", function (value, element, regexp) {
        let re = new RegExp(regexp);
        return this.optional(element) || re.test(value);
    }, "Invalid format");

    $("#resetPassword").validate({
        rules: {

            password: {
                required: true,
                minlength: 8,
                regex:"^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$"
            },
            confirmPassword: {
                required: true,
                equalTo: "#password"
            }

        },
        messages: {
            password: {
                required: "!! Password Required !!",
                minlength: "Password must be at least 8 characters",
                regex: "Password must be at least 8 characters and include uppercase, lowercase, number, and special character"
            },
            confirmPassword: {
                required: "!! Confirm Password Required !!",
                equalTo: "Passwords do not match"
            }
        },
        errorClass: "text-danger fw-semibold",
        errorElement: "small"
    });
});


$(function () {
//Place Order
    $.validator.addMethod("regex", function (value, element, regexp) {
        let re = new RegExp(regexp);
        return this.optional(element) || re.test(value);
    }, "Invalid format");

    $("#Orders").validate({
        rules: {
            firstName: {
                required: true,
                regex: "^[A-Za-z ]{3,30}$"
            },
            lastName: {
                required: true,
                regex: "^[A-Za-z ]{3,30}$"
            },
           email: {
                required: true,
                regex: "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$"
            },
            moblieNo: {
                required: true,
                regex: "^[6-9]\\d{9}$"
            },
            pincode: {
                required: true,
                regex: "^\\d{6}$"
            },
            Address: {
                required: true,
                minlength: 5
            },
            city: {
                required: true,
                regex: "^[A-Za-z ]+$"
            },
            state: {
                required: true,
                regex: "^[A-Za-z ]+$"
            },
            paymentType:{
                required:true,
            }

        },
        messages: {
            firstName: {
                required: "!! First Name Required !!",
                regex: "Only alphabets and spaces allowed"
            },
            lastName: {
                required: "!! Last Name Required !!",
                regex: "Only alphabets and spaces allowed"
            },
            email: {
                required: "!! Email Required !!",
                regex: "Invalid email format"
            },
            moblieNo: {
                required: "!! Mobile No Required !!",
                regex: "Enter a valid 10-digit mobile number starting with 6-9"
            },

            pincode: {
                required: "!! Pincode Required !!",
                regex: "Enter 6 digit pincode"
            },
            Address: {
                required: "!! Address Required !!",
                minlength: "Enter full address"
            },
            city: {
                required: "!! City Required !!",
                regex: "Only alphabets allowed"
            },
            state: {
                required: "!! State Required !!",
                regex: "Only alphabets allowed"
            },
            paymentType:{
                required:"!! Please select any one option !!",
            }

        },
        errorClass: "text-danger fw-semibold",
        errorElement: "small"
    });
});

$(function () {
//add Product
    $.validator.addMethod("regex", function (value, element, regexp) {
        let re = new RegExp(regexp);
        return this.optional(element) || re.test(value);
    }, "Invalid format");

    $("#addProduct").validate({
        rules: {
            productName: {
                required: true,
                regex: "^[A-Za-z ]{3,30}$"
            },
            price: {
                required: true,
                regex: /^\d+(\.\d{1,2})?$/
            },
            file:{
                required:true,
            },
            discount: {
                required: true,
                regex: /^(100|[1-9]?[0-9])$/
            },
            description: {
                required: true,
                minlength: 10
            },
            quantity: {
                required: true,
                regex: /^[1-9][0-9]*$/
            },
            category: {
                required: true,
            }
        },
        messages: {
            productName: {
                required: "!! Name Required !!",
                regex: "Only alphabets and spaces allowed"
            },
            price: {
                required: "!! Price Required !!",
                regex: "Enter valid price (e.g. 100 or 99.99)"
            },
            file:{
               required:"!! Image Required !!",
            },
            discount: {
                required: "!! Discount is required !!",
                regex: "Enter valid discount (0–100%)"
            },
            description: {
                required: "!! Description required !!",
                minlength: "Description must be at least 10 characters"
            },
            quantity: {
                required: "!! Quantity required !!",
                regex: "Enter valid quantity (1 or more)"
            },
            category: {
                required: "!! Category required !!",
            }

        },
        errorClass: "text-danger fw-semibold",
        errorElement: "small"
    });
});
