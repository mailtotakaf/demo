package com.example.demo.form

import org.jetbrains.annotations.NotNull
import org.springframework.lang.NonNull
import org.springframework.lang.Nullable
import javax.validation.constraints.Max
import javax.validation.constraints.NotEmpty


class InsertForm {

    @Max(10)
    @NotEmpty
    var title: String? = null

    @NotEmpty
    var textArea: String? = null

    @NotEmpty
    var mail: String? = null

}