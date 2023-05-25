import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ArtistService} from '../../../core/services/artist.service';
import {Router} from '@angular/router';
import {HttpErrorResponse} from '@angular/common/http';

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.sass']
})
export class LoginComponent implements OnInit {

    loginGroup!: FormGroup;
    loginButtonPressed: boolean = false;
    errorMessage = '';
    passwordTextType = false;

    constructor(
        private readonly _router: Router,
        private readonly _fb: FormBuilder,
        private readonly _artistService: ArtistService
    ) {
    }

    ngOnInit(): void {
        this.buildForm();
    }

    login = (): void => {
        this.loginButtonPressed = true;

        this._artistService.login({
            username: this.loginGroup.value.username,
            password: this.loginGroup.value.password
        }).subscribe({
            next: () => this._router.navigate(['/']),
            error: (err: HttpErrorResponse) => {
                if (err.status === 403) {
                    this.setErrorMessage('Invalid credentials');
                    this.loginButtonPressed = false;
                }
            }
        })
    }

    isNotValid = (field: string): boolean => {
        const formField = this.loginGroup.get(field);
        if (formField == undefined) return false
        return formField.invalid && (formField.dirty || formField.touched);
    }

    private buildForm = (): void => {
        this.loginGroup = this._fb.group({
            username: ['', Validators.compose([
                Validators.required,
                Validators.minLength(1),
                Validators.maxLength(50)])
            ],
            password: ['', Validators.compose([
                Validators.required,
                Validators.minLength(1)])
            ]
        });
    }

    isPasswordEmpty = (): boolean =>
        this.loginGroup.value.password.trim().length === 0;

    showPassword = (): void => {
        this.passwordTextType = true;
        setTimeout(() => {
            this.passwordTextType = false;
        }, 1000);
    }

    private setErrorMessage = (message: string): void => {
        this.errorMessage = message;
        setTimeout(() => {
            this.errorMessage = '';
        }, 5000);
    }
}