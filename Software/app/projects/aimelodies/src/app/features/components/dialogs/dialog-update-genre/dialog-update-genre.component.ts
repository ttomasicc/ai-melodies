import {Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Genre} from '../../../../core/models/domain/genre';
import {GenreResponse} from '../../../../core/models/responses/genre.response';
import {HttpErrorResponse} from '@angular/common/http';
import {GenreService} from '../../../../core/services/genre.service';

@Component({
    selector: 'app-dialog-update-genre[value]',
    templateUrl: './dialog-update-genre.component.html',
    styleUrls: ['./dialog-update-genre.component.sass']
})
export class DialogUpdateGenreComponent implements OnInit, OnChanges {
    @Input('open') dialogOpen: boolean = false;

    @Input('value') set genre(value: Genre) {
        this._genre = value;
        this.genreFormGroup?.patchValue({
            name: value.name
        });
    }

    @Output('success') successEmitter: EventEmitter<Genre> = new EventEmitter<Genre>();
    @Output('close') closeEmitter: EventEmitter<void> = new EventEmitter<void>();

    genreFormGroup!: FormGroup;
    errorMessage = '';

    private _genre!: Genre;

    constructor(
        private readonly _fb: FormBuilder,
        private readonly _genreService: GenreService
    ) {
    }

    ngOnInit(): void {
        this.buildForm();
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
        }
    }

    updateGenre = (): void => {
        this.errorMessage = '';
        this._genreService.update(
            this._genre._links.self.href,
            {name: this.genreFormGroup.value.name}
        ).subscribe({
            next: (genre: GenreResponse) => this.successEmitter.emit(genre as Genre),
            error: (err: HttpErrorResponse) => this.setErrorMessage(err.error.message)
        });
    }

    closeDialog = (): boolean => {
        this.closeEmitter.emit();
        this.errorMessage = '';
        return false;
    }

    isNotValid = (field: string): boolean => {
        const formField = this.genreFormGroup.get(field);
        if (formField == undefined) return false
        return formField.invalid && (formField.dirty || formField.touched);
    }

    private buildForm = (): void => {
        this.genreFormGroup = this._fb.group({
            name: ['', Validators.compose([
                Validators.required,
                Validators.minLength(1),
                Validators.maxLength(50)])
            ]
        });
    }

    private resetForm = (): void => {
        this.genreFormGroup.disable();
        this.genreFormGroup.reset();
        this.genreFormGroup.enable();
    }

    private setErrorMessage = (message: string): void => {
        this.errorMessage = message;
        setTimeout(() => {
            this.errorMessage = '';
        }, 5000);
    }
}