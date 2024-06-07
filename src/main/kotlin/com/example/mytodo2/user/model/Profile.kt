package com.example.mytodo2.user.model

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
class Profile(
    @Column(name = "nickname", nullable = false)
    var nickname: String,
)