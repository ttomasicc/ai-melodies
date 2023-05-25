import {Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges} from '@angular/core';
import {Genre} from '../../../../core/models/domain/genre';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {HttpErrorResponse} from '@angular/common/http';
import {Melody} from '../../../../core/models/domain/melody';
import {MelodyService} from '../../../../core/services/melody.service';
import {GenreService} from '../../../../core/services/genre.service';
import {take} from 'rxjs';

@Component({
    selector: 'app-dialog-add-melody[path]',
    templateUrl: './dialog-add-melody.component.html',
    styleUrls: ['./dialog-add-melody.component.sass']
})
export class DialogAddMelodyComponent implements OnInit, OnChanges {
    @Input('path') path!: string;
    @Input('open') dialogOpen: boolean = false;

    @Output('success') successEmitter: EventEmitter<Melody> = new EventEmitter<Melody>();
    @Output('close') closeEmitter: EventEmitter<void> = new EventEmitter<void>();

    allGenres: Genre[] = [];
    melodyFormGroup!: FormGroup;
    saveButtonPressed: boolean = false;
    errorMessage = '';

    private fileEvent?: any;

    constructor(
        private readonly _fb: FormBuilder,
        private readonly _melodyService: MelodyService,
        private readonly _genreService: GenreService
    ) {
    }

    async ngOnInit(): Promise<void> {
        this.buildForm();
        this.allGenres = await this.getAllGenres();
    }

    /**
     * Clears form on dialog close action
     * @param changes
     */
    ngOnChanges(changes: SimpleChanges): void {
        if (changes['dialogOpen']                   // exists
            && !changes['dialogOpen'].currentValue  // is closing
            && !changes['dialogOpen'].firstChange   // is not first change
        ) {
            this.resetForm();
            if (this.fileEvent) this.fileEvent.target.value = null;
            this.saveButtonPressed = false;
        }
    }

    onFileChange = (event: any) => {
        this.fileEvent = event;
        const [file] = event.target.files;

        this.melodyFormGroup.patchValue({
            audio: file ?? null
        });
    }

    addMelody = (): void => {
        this.saveButtonPressed = true;
        this.errorMessage = '';

        this._melodyService
            .add(this.path, this.buildRequest())
            .subscribe({
                next: (melody) => this.successEmitter.emit(melody),
                error: (err: HttpErrorResponse) => {
                    this.saveButtonPressed = false;
                    this.setErrorMessage(err.error.message);
                }
            });
    }

    closeDialog = (): boolean => {
        this.closeEmitter.emit();
        this.errorMessage = '';
        return false;
    }

    isNotValid = (field: string): boolean => {
        const formField = this.melodyFormGroup.get(field);
        if (formField == undefined) return false;
        return formField.invalid && (formField.dirty || formField.touched);
    }

    private buildRequest = (): FormData => {
        const formData: FormData = new FormData();

        for (const field in this.melodyFormGroup.controls)
            formData.append(field, this.melodyFormGroup?.get(field)?.value);

        return formData;
    }

    private buildForm = (): void => {
        this.melodyFormGroup = this._fb.group({
            name: ['', Validators.compose([
                Validators.required,
                Validators.minLength(1),
                Validators.maxLength(100)])
            ],
            genreId: [null, Validators.compose([Validators.required])],
            audio: [null, Validators.compose([Validators.required])]
        });
    }

    private resetForm = (): void => {
        this.melodyFormGroup.disable();
        this.melodyFormGroup.reset();
        this.melodyFormGroup.enable();
    }

    private getAllGenres = (): Promise<Genre[]> =>
        new Promise((resolve, reject) => {
            this._genreService.getAll()
                .pipe(take(1))
                .subscribe({
                    next: (response) => resolve(response._embedded?.genres ?? []),
                    error: (err) => reject(err)
                });
        });

    private setErrorMessage = (message: string): void => {
        this.errorMessage = message;
        setTimeout(() => {
            this.errorMessage = '';
        }, 5000);
    }
}