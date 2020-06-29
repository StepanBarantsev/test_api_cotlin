package ru.task.suites

import org.junit.runner.RunWith
import org.junit.platform.suite.api.SelectPackages
import org.junit.platform.runner.JUnitPlatform

@RunWith(JUnitPlatform::class)
@SelectPackages("ru.task.tests.Accounts")
class AllTests {}