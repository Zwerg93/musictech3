import {Component} from '@angular/core';
import {FormBuilder, Validators} from '@angular/forms';
import {HttpService} from "../../_services/http.service";
import {UserModel} from "../../model/user.model";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
  addressForm = this.fb.group({
    firstname: [null, Validators.required],
    lastname: [null, Validators.required],
    username: [null, Validators.required],
    mail: [null, Validators.required],
    password: [null, Validators.compose([
      Validators.required, Validators.minLength(3), Validators.maxLength(20)])
    ]

  });
  isSuccessful = false;
  isSignUpFailed = false;
  errorMessage = '';
  user!: UserModel
  hasUnitNumber = false;


  constructor(private fb: FormBuilder, private http: HttpService) {
  }

  onSubmit(): void {

    // @ts-ignore
    const {firstname, lastname, username, mail, password} = this.addressForm.getRawValue();

    console.log(firstname)
    this.http.register(username!, firstname!, lastname!, password!, mail!).subscribe({
      next: data => {
        this.isSuccessful = true;
        this.isSignUpFailed = false;
      },
      error: err => {
        this.errorMessage = err.error.message;
        this.isSignUpFailed = true;
      }
    })
  }
}
