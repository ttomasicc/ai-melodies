import {Component, HostListener, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {ArtistService} from '../../../core/services/artist.service';
import {HttpErrorResponse} from '@angular/common/http';
import {ErrorResponse} from '../../../core/models/responses/error.response';

@Component({
    selector: 'app-registration',
    templateUrl: './registration.component.html',
    styleUrls: ['./registration.component.sass']
})
export class RegistrationComponent implements OnInit {

    registrationGroup!: FormGroup;
    registerButtonPressed: boolean = false;
    errorMessage = '';
    passwordTextType = false;
    dialogOpen = false;

    constructor(
        private readonly _router: Router,
        private readonly _fb: FormBuilder,
        private readonly _artistService: ArtistService
    ) {
    }

    ngOnInit(): void {
        this.buildForm();
    }

    @HostListener('document:keydown.space', ['$event'])
    onSpaceDown(event: KeyboardEvent) {
        event.preventDefault();
    }

    register = (): void => {
        this.registerButtonPressed = true;

        this._artistService.register({
            username: this.registrationGroup.value.username,
            email: this.registrationGroup.value.email,
            password: this.registrationGroup.value.password
        }).subscribe({
            next: () => {
                this.dialogOpen = true;
                setTimeout(() => this._router.navigate(['/login']), 3000);
            },
            error: (err: HttpErrorResponse) => {
                if (err.status === 400) {
                    this.setErrorMessage((err.error as ErrorResponse).message);
                    this.registerButtonPressed = false;
                }
            }
        })
    }

    isNotValid = (field: string): boolean => {
        const formField = this.registrationGroup.get(field);
        if (formField == undefined) return false
        return formField.invalid && (formField.dirty || formField.touched);
    }

    isPasswordEmpty = (): boolean =>
        this.registrationGroup.value.password.trim().length === 0;

    showPassword = (): void => {
        this.passwordTextType = true;
        setTimeout(() => {
            this.passwordTextType = false;
        }, 1000);
    }

    private buildForm = (): void => {
        this.registrationGroup = this._fb.group({
            username: ['', Validators.compose([
                Validators.required,
                Validators.minLength(1),
                Validators.maxLength(50)])
            ],
            email: ['', Validators.compose([
                Validators.required,
                Validators.email,
                Validators.maxLength(70)
            ])],
            password: ['', Validators.compose([
                Validators.required,
                Validators.minLength(1)])
            ]
        });
    }

    private setErrorMessage = (message: string): void => {
        this.errorMessage = message;
        setTimeout(() => {
            this.errorMessage = '';
        }, 5000);
    }
}