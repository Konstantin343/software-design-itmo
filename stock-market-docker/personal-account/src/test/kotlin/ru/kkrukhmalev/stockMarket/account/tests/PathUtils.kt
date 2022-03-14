package ru.kkrukhmalev.stockMarket.account.tests

import java.nio.file.Path
 
operator fun Path.div(other: String) = this.resolve(other)

operator fun String.div(other: String) = Path.of(this).resolve(other)