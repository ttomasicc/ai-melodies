import {Component, HostListener, OnInit} from '@angular/core';
import {Artist} from '../../../../core/models/domain/artist';
import {ArtistService} from '../../../../core/services/artist.service';
import {Router} from '@angular/router';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {take} from 'rxjs';

@Component({
    selector: 'app-profile-update',
    templateUrl: './profile-update.component.html',
    styleUrls: ['./profile-update.component.sass']
})
export class ProfileUpdateComponent implements OnInit {
    artistFormGroup!: FormGroup;
    saveButtonPressed: boolean = false;
    passwordTextType = false;
    errorMessage = '';
    artist?: Artist;

    constructor(
        private readonly _router: Router,
        private readonly _fb: FormBuilder,
        private readonly _artistService: ArtistService
    ) {
    }

    @HostListener('document:keydown.escape', ['$event'])
    async onEscapeKeyDown(event: KeyboardEvent) {
        await this._router.navigate(['/profile']);
    }

    async ngOnInit(): Promise<void> {
        this.artist = await this.getCurrentArtist();
        this.buildForm();
    }

    saveChanges = async (): Promise<void> => {
        this.saveButtonPressed = true;

        const request = this.buildRequest();
        if (request) {
            this.updateArtist(request)
                .then(async () => await this._router.navigate(['/profile']))
                .catch((err) => {
                    this.setErrorMessage(err);
                    this.saveButtonPressed = false;
                });
        } else {
            await this._router.navigate(['/profile']);
        }
    }

    onFileChange = (event: any) => {
        const [file] = event.target.files;

        this.artistFormGroup.patchValue({
            image: file ?? null
        });
    }

    isNotValid = (field: string): boolean => {
        const formField = this.artistFormGroup.get(field);
        if (formField == undefined) return false
        return formField.invalid && (formField.dirty || formField.touched);
    }

    isPasswordEmpty = (): boolean =>
        this.artistFormGroup.value.password.trim().length === 0;

    showPassword = (): void => {
        this.passwordTextType = true;
        setTimeout(() => {
            this.passwordTextType = false;
        }, 1000);
    }

    private buildForm = (): void => {
        this.artistFormGroup = this._fb.group({
            image: [null],
            bio: [this.artist?.bio ?? ''],
            firstName: [this.artist?.firstName ?? '', Validators.compose([
                Validators.maxLength(70)
            ])],
            lastName: [this.artist?.lastName ?? '', Validators.compose([
                Validators.maxLength(70)
            ])],
            email: [this.artist?.email ?? '', Validators.compose([
                Validators.email,
                Validators.maxLength(70)
            ])],
            password: ['']
        });
    }

    private buildRequest = (): FormData | undefined => {
        const formData: FormData = new FormData();

        for (const field in this.artistFormGroup.controls) {
            if (this.validateFormField(field))
                formData.append(field, this.artistFormGroup?.get(field)?.value);
        }

        return (formData as any).entries().next().done ? undefined : formData;
    }

    private validateFormField = (fieldName: string): boolean =>
        this.artistFormGroup.get(fieldName)?.value
        && this.artistFormGroup.get(fieldName)?.value !== this.artist!![fieldName as keyof typeof this.artist]

    private setErrorMessage = (message: string) => {
        this.errorMessage = message;
        setTimeout(() => {
            this.errorMessage = '';
        }, 5000);
    }

    private getCurrentArtist = (): Promise<Artist | undefined> =>
        new Promise((resolve, reject) => {
            this._artistService
                .getCurrent()
                .subscribe({
                    next: (artist) => resolve(artist),
                    error: (err) => reject(err)
                });
        });

    private updateArtist = (request: FormData): Promise<void> =>
        new Promise((resolve, reject) => {
            this._artistService.update(
                this.artist?._links.profile?.href ?? '',
                request
            )
                .pipe(take(1))
                .subscribe({
                    next: _ => resolve(),
                    error: (err) => reject(err.error.message)
                });
        });
}