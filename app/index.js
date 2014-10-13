/**
 * Created by corbett on 9/29/14.
 */
'use strict';

var path = require('path'),
    util = require('util'),
    yeoman = require('yeoman-generator'),
    chalk = require('chalk'),
    _string = require('underscore.string'),
    shelljs = require('shelljs'),
    mkdirp = require('mkdirp'),
    packagejs = require(__dirname + '/../package.json');

var SpringRestGenerator = module.exports = function SpringRestGenerator(args, options, config) {

    yeoman.generators.Base.apply(this, arguments);

    this.on('end', function() {
        console.log('This is the end...my only friend the end.');
    });

    this.pkg = JSON.parse(this.readFileAsString(path.join(__dirname, '../package.json')));
};

// node's util module: inherit yeoman's base generator construct into ours
util.inherits(SpringRestGenerator, yeoman.generators.Base);

SpringRestGenerator.prototype.askFor = function askFor() {
    console.log(chalk.magenta('SpringRestGenerator in da house!\n'));
    var d = new Date,
        dformat = [d.getMonth()+1,
                   d.getDate(),
                   d.getFullYear()].join('/')+' '+
                  [d.getHours(),
                   d.getMinutes(),
                   d.getSeconds()].join(':');
    // --------------------------------------------------
    // config used for testing only
    // these values need to be obtained from command line
    // --------------------------------------------------
    this.app = {};
    this.app.projectName = 'agreatproject';
    this.app.createdOn = dformat;
    this.app.createdBy = 'Blue Eye Bucky';
    this.app.packageName = 'net.cworks.great';
    this.app.javaVersion = '8';
    this.app.group = 'net-cworks'
    this.app.version = '1.0.0';
    this.app.description = 'A superduper app';
    this.app.mainClass = this.app.packageName + ".Application";
};

SpringRestGenerator.prototype.app = function app() {

    var topPackage = this.app.packageName.replace(/\./g, '/');
    // create app project folders
    mkBaseProject(this.app.projectName, topPackage);

    // lets create the application
    this.template('_yo_bower.json', this.app.projectName + '/bower.json');
    this.template('_yo_bowerrc', this.app.projectName + '/bowerrc');
    this.template('_yo_build.gradle', this.app.projectName + '/build.gradle');
    this.template('_yo_editorconfig', this.app.projectName  + '/.editorconfig');
    this.template('_yo_gitattributes', this.app.projectName + '/.gitattributes');
    this.template('_yo_gitignore', this.app.projectName + '/.gitignore');
    this.template('_yo_gradle.properties', this.app.projectName  + '/gradle.properties');
    this.template('_yo_gradlew', this.app.projectName  + '/gradlew');
    this.template('_yo_gradlew.bat', this.app.projectName  + '/gradlew.bat');
    this.template('_yo_profile_dev.gradle', this.app.projectName + '/profile_dev.gradle');
    this.template('_yo_profile_prod.gradle', this.app.projectName + '/profile_prod.gradle');
    this.template('_yo_README.md', this.app.projectName + '/README.md');

};

function mkBaseProject(projectName, topPackage) {
    var srcMainDir    = 'src/main/java';
    var srcTestDir    = 'src/test/java';
    var resMainDir    = 'src/main/resources';
    var resTestDir    = 'src/test/resources';
    var webappDir     = 'src/main/webapp';

    // create app project folders
    console.log(chalk.cyan(projectName));
    shelljs.mkdir(projectName);
    console.log(chalk.cyan(projectName + '/' + srcMainDir));
    mkdirp(projectName + '/' + srcMainDir);
    console.log(chalk.cyan(projectName + '/' + srcMainDir + '/' + topPackage));
    mkdirp(projectName + '/' + srcMainDir + '/' + topPackage);
    console.log(chalk.cyan(projectName + '/' + srcTestDir));
    mkdirp(projectName + '/' + srcTestDir);
    console.log(chalk.cyan(projectName + '/' + srcTestDir + '/' + topPackage));
    mkdirp(projectName + '/' + srcTestDir + '/' + topPackage);
    console.log(chalk.cyan(projectName + '/' + resMainDir));
    mkdirp(projectName + '/' + resMainDir);
    console.log(chalk.cyan(projectName + '/' + resTestDir));
    mkdirp(projectName + '/' + resTestDir);
    console.log(chalk.cyan(projectName + '/' + webappDir));
    mkdirp(projectName + '/' + webappDir);
}