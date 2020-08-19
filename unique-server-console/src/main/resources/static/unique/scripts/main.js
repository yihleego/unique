'use strict'

const api = {
    console: {
        save(params, callback) {
            $post(`/consoles/sequences`, params, callback);
        },
        update(params, callback) {
            $put(`/consoles/sequences/${params.key}`, params, callback);
        },
        delete(params, callback) {
            $delete(`/consoles/sequences/${params.key}`, params, callback);
        },
        list(params, callback) {
            $get(`/consoles/sequences`, params, callback);
        },
        get(params, callback) {
            $get(`/consoles/sequences/${params.key}`, params, callback);
        },
        getSnapshot(params, callback) {
            $get(`/consoles/sequences/${params.key}/snapshot`, params, callback);
        },
        skip(params, callback) {
            $patch(`/consoles/sequences/${params.key}/skip/${params.size}`, params, callback);
        },
        load(params, callback) {
            $put(`/consoles/configurations/${params.key}`, params, callback);
        },
        getMode(params, callback) {
            $get(`/consoles/modes`, params, callback);
        },
    },
};
const monitor = {
    template: `
<div class="monitor overflow-hidden">
    <v-app-bar absolute
               elevate-on-scroll
               scroll-target="#scrolling-techniques"
               color="white">
        <v-toolbar-title>
            <span class="navbar-title">Unique Console</span><span class="navbar-mode">{{mode}}</span>
        </v-toolbar-title>
        <v-spacer></v-spacer>
        <v-text-field
                v-model="search"
                append-icon="mdi-magnify"
                label="Search"
                hide-details>
        </v-text-field>
        <v-btn icon @click="openSaveDialog">
            <v-icon>mdi-plus</v-icon>
        </v-btn>
        <!--<v-btn icon @click="expandAll">
            <v-icon>mdi-arrow-expand-vertical</v-icon>
        </v-btn>-->
        <v-btn icon @click="collapseAll" v-if="standalone">
            <v-icon>mdi-arrow-collapse-vertical</v-icon>
        </v-btn>
    </v-app-bar>
    <v-card id="scrolling-techniques" class="monitor-main">
        <v-data-table id="scrolling-sheet"
                      :loading="loading"
                      loading-text="Please wait"
                      :headers="headers"
                      :items="sequences"
                      :search="search"
                      :single-expand="singleExpand"
                      :expanded.sync="expanded"
                      item-key="key"
                      @item-expanded="itemExpanded"
                      :show-expand="showExpand"
                      dense
                      disable-pagination="true"
                      hide-default-footer="true">
            <template v-slot:item.operation="{ item }">
                <v-tooltip top open-delay="500">
                    <template v-slot:activator="{ on, attrs }">
                        <v-btn icon @click="get(item.key)" :disabled="item.loading" v-bind="attrs" v-on="on">
                            <v-icon v-if="!item.loading">
                                mdi-refresh
                            </v-icon>
                            <v-progress-circular
                                    v-else
                                    indeterminate
                                    :size="20">
                            </v-progress-circular>
                        </v-btn>
                    </template>
                    <span>Refresh</span>
                </v-tooltip>
                <v-tooltip top open-delay="500">
                    <template v-slot:activator="{ on, attrs }">
                        <v-btn icon @click="openUpdateDialog(item.key)" v-bind="attrs" v-on="on">
                            <v-icon> mdi-pencil-outline</v-icon>
                        </v-btn>
                    </template>
                    <span>Edit</span>
                </v-tooltip>
                <v-tooltip top open-delay="500">
                    <template v-slot:activator="{ on, attrs }">
                        <v-btn icon @click="openSkipDialog(item.key)" v-bind="attrs" v-on="on" v-if="!standalone">
                            <v-icon>mdi-skip-forward-outline</v-icon>
                        </v-btn>
                    </template>
                    <span>Skip</span>
                </v-tooltip>
                <v-tooltip top open-delay="500">
                    <template v-slot:activator="{ on, attrs }">
                        <v-btn icon @click="openDeleteDialog(item.key)" v-bind="attrs" v-on="on">
                            <v-icon>
                                mdi-delete-forever-outline
                            </v-icon>
                        </v-btn>
                    </template>
                    <span>Delete</span>
                </v-tooltip>
            </template>
            <template v-slot:item.cache="{ item }">
                <span v-bind:class="!standalone ? 'font-grey' : ''">{{item.cache}}</span>
            </template>
            <template v-slot:expanded-item="{ headers, item }">
                <td :colspan="headers.length">
                    <v-row>
                        <v-col :cols="6">
                            <v-card>
                                <v-card-title class="subheading font-weight-bold title">
                                    {{ item.key }}
                                    <v-chip class="ma-2"
                                            color="primary"
                                            text-color="white"
                                            small>
                                        Database
                                    </v-chip>
                                    <v-chip class="ma-2"
                                            color="primary"
                                            text-color="white"
                                            small>
                                        <v-avatar left class="primary darken-3">
                                            Ver
                                        </v-avatar>
                                        {{ item.version }}
                                    </v-chip>
                                    <v-tooltip top open-delay="500">
                                        <template v-slot:activator="{ on, attrs }">
                                            <v-btn icon color="primary" @click="get(item.key)" :disabled="item.loading" v-bind="attrs" v-on="on">
                                                <v-icon v-if="!item.loading">
                                                    mdi-refresh
                                                </v-icon>
                                                <v-progress-circular
                                                        v-else
                                                        indeterminate
                                                        :size="20"
                                                        color="primary">
                                                </v-progress-circular>
                                            </v-btn>
                                        </template>
                                        <span>Refresh</span>
                                    </v-tooltip>
                                </v-card-title>
                                <v-divider></v-divider>
                                <v-list dense rounded>
                                    <v-list-item>
                                        <v-list-item-content>Value</v-list-item-content>
                                        <v-list-item-content>{{ item.value }}</v-list-item-content>
                                        <v-list-item-content></v-list-item-content>
                                        <v-list-item-content></v-list-item-content>
                                    </v-list-item>
                                    <v-list-item>
                                        <v-list-item-content>Increment</v-list-item-content>
                                        <v-list-item-content>{{ item.increment }}</v-list-item-content>
                                        <v-list-item-content>Cache Size</v-list-item-content>
                                        <v-list-item-content>{{ item.cache }}</v-list-item-content>
                                    </v-list-item>
                                </v-list>
                            </v-card>
                        </v-col>
                        <v-col :cols="6">
                            <v-card v-if="item.hasSnapshot">
                                <v-card-title class="subheading font-weight-bold title">
                                    <span> {{ item.snapshot.key }}</span>
                                    <v-chip class="ma-2"
                                            color="green"
                                            text-color="white"
                                            small>
                                        Memory
                                    </v-chip>
                                    <v-chip class="ma-2"
                                            color="green"
                                            text-color="white"
                                            small>
                                        <v-avatar left class="green darken-3">
                                            Ver
                                        </v-avatar>
                                        {{ item.snapshot.version }}
                                    </v-chip>
                                    <v-chip class="ma-2"
                                            color="green"
                                            text-color="white"
                                            small>{{ item.snapshot.snapshotTime }}
                                    </v-chip>
                                    <v-tooltip top open-delay="500">
                                        <template v-slot:activator="{ on, attrs }">
                                            <v-btn icon color="green" @click="getSnapshot(item.key)"
                                                   :disabled="item.loadingSnapshot"
                                                   v-bind="attrs" v-on="on">
                                                <v-icon v-if="!item.loadingSnapshot">mdi-refresh</v-icon>
                                                <v-progress-circular
                                                        v-else
                                                        indeterminate
                                                        :size="20"
                                                        color="green">
                                                </v-progress-circular>
                                            </v-btn>
                                        </template>
                                        <span>Refresh</span>
                                    </v-tooltip>
                                    <v-tooltip top open-delay="500">
                                        <template v-slot:activator="{ on, attrs }">
                                            <v-btn icon color="green" @click="openLoadDialog(item.key)"
                                                   :disabled="item.loadingSnapshot || item.version === item.snapshot.version"
                                                   v-bind="attrs" v-on="on">
                                                <v-icon>mdi-database-sync</v-icon>
                                            </v-btn>
                                        </template>
                                        <span>Load into memory</span>
                                    </v-tooltip>
                                    <v-tooltip top open-delay="500">
                                        <template v-slot:activator="{ on, attrs }">
                                            <v-btn icon color="green" @click="openSkipDialog(item.key)"
                                                   v-bind="attrs" v-on="on">
                                                <v-icon>mdi-skip-forward-outline</v-icon>
                                            </v-btn>
                                        </template>
                                        <span>Skip</span>
                                    </v-tooltip>
                                </v-card-title>
                                <v-divider></v-divider>
                                <v-list dense rounded>
                                    <v-list-item>
                                        <v-list-item-content>Current Value</v-list-item-content>
                                        <v-list-item-content>{{ item.snapshot.cur }}</v-list-item-content>
                                        <v-list-item-content>Max Value</v-list-item-content>
                                        <v-list-item-content>{{ item.snapshot.max }}</v-list-item-content>
                                    </v-list-item>
                                    <v-list-item>
                                        <v-list-item-content>Increment</v-list-item-content>
                                        <v-list-item-content>{{ item.snapshot.increment }}</v-list-item-content>
                                        <v-list-item-content>Cache Size</v-list-item-content>
                                        <v-list-item-content>{{ item.snapshot.cache }}</v-list-item-content>
                                    </v-list-item>
                                </v-list>
                            </v-card>
                            <v-card v-else class="snapshot-loading">
                                <v-card-text class="text-center" v-if="!item.loadingSnapshot">
                                    There is no sequence
                                    <v-chip small>{{item.key}}</v-chip>
                                    in memory.
                                </v-card-text>
                                <v-btn color="green " dark @click="load(item.key)" v-if="!item.loadingSnapshot">
                                    load into memory
                                </v-btn>
                                <v-progress-circular
                                        v-if="item.loadingSnapshot"
                                        indeterminate
                                        color="green">
                                </v-progress-circular>
                            </v-card>
                        </v-col>
                    </v-row>
                </td>
            </template>
        </v-data-table>
    </v-card>
    <v-dialog v-model="showSaveDialog" max-width="600px">
        <v-card>
            <v-card-title>
                <span class="headline">New Sequence</span>
            </v-card-title>
            <v-card-text>
                <v-row>
                    <v-col cols="12" sm="6" md="6">
                        <v-text-field label="Key" required outlined clearable dense autofocus="true" counter="100"
                                      :rules="rules.key" v-model="saveSequence.key"
                                      @keyup.ctrl.enter.native="save">
                        </v-text-field>
                    </v-col>
                    <v-col cols="12" sm="6" md="6">
                        <v-text-field label="Value" required outlined clearable dense
                                      :rules="rules.value" v-model="saveSequence.value" type="number" min="0"
                                      @keyup.ctrl.enter.native="save">
                        </v-text-field>
                    </v-col>
                    <v-col cols="12" sm="6" md="6">
                        <v-text-field label="Increment" required outlined clearable dense
                                      :rules="rules.increment" v-model="saveSequence.increment" type="number" min="1"
                                      @keyup.ctrl.enter.native="save">
                        </v-text-field>
                    </v-col>
                    <v-col cols="12" sm="6" md="6">
                        <v-text-field label="Cache Size" required outlined clearable dense
                                      :rules="rules.cache" v-model="saveSequence.cache" type="number" min="1" :disabled="!standalone"
                                      @keyup.ctrl.enter.native="save">
                        </v-text-field>
                    </v-col>
                </v-row>
            </v-card-text>
            <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="blue darken-1" text @click="showSaveDialog = false">Close</v-btn>
                <v-btn color="blue darken-1" text @click="save">Save</v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
    <v-dialog v-model="showUpdateDialog" max-width="600px">
        <v-card>
            <v-card-title>
                <span class="headline">Update Sequence <v-chip>{{updateSequence.key}}</v-chip></span>
            </v-card-title>
            <v-card-text>
                <v-row>
                    <v-col cols="12" sm="6" md="6">
                        <v-text-field label="Increment" required outlined clearable dense
                                      :rules="rules.increment" v-model="updateSequence.increment" type="number" min="1"
                                      @keyup.ctrl.enter.native="update">
                        </v-text-field>
                    </v-col>
                    <v-col cols="12" sm="6" md="6">
                        <v-text-field label="Cache Size" required outlined clearable dense
                                      :rules="rules.cache" v-model="updateSequence.cache" type="number" min="1" :disabled="!standalone"
                                      @keyup.ctrl.enter.native="update">
                        </v-text-field>
                    </v-col>
                </v-row>
            </v-card-text>
            <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="blue darken-1" text @click="showUpdateDialog = false">Close</v-btn>
                <v-btn color="blue darken-1" text @click="update">Update</v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
    <v-dialog v-model="showDeleteDialog" max-width="600px">
        <v-card>
            <v-card-title class="headline lighten-2">
                Confirm
            </v-card-title>
            <v-card-text>
                This action cannot be undone. Please type <span style="background: #e0e0e0; word-break: break-all;">{{deleteSequence.key}}</span> to delete the sequence.
                <v-text-field dense color="red" v-model="deleteSequence.input"></v-text-field>
            </v-card-text>
            <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="red darken-1" text @click="showDeleteDialog = false">Close</v-btn>
                <v-btn color="red darken-1" text @click="remove(deleteSequence.key)"
                       :disabled="!deleteSequence.input || deleteSequence.key != deleteSequence.input.trim()">Delete</v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
    <v-dialog v-model="showSkipDialog" max-width="600px">
        <v-card>
            <v-card-title>
                <span class="headline">Skip Sequence <v-chip>{{skipSequence.key}}</v-chip></span>
            </v-card-title>
            <v-card-text>
                <v-row>
                    <v-col cols="12">
                        <v-text-field label="Size" required outlined clearable dense
                                      :rules="rules.size" v-model="skipSequence.size" type="number" min="1"
                                      @keyup.ctrl.enter.native="skip" @input="changeSkipSize">
                        </v-text-field>
                    </v-col>
                    <v-col cols="12">
                        <div class="d-flex justify-space-between">
                            <div>
                                <div class="grey--text mb-2 text-center">Size</div>
                                <div class="font-weight-medium text-center">{{skipSequence.size?skipSequence.size:'0'}}</div>
                            </div>
                            <div>
                                <div class="grey--text mb-2 text-center">&nbsp;</div>
                                <div class="font-weight-medium text-center">×</div>
                            </div>
                            <div>
                                <div class="grey--text mb-2 text-center">Increment</div>
                                <div class="font-weight-medium text-center">{{skipSequence.increment}}</div>
                            </div>
                            <div>
                                <div class="grey--text mb-2 text-center">&nbsp;</div>
                                <div class="font-weight-medium text-center">+</div>
                            </div>
                            <div>
                                <div class="grey--text mb-2 text-center">Current</div>
                                <div class="font-weight-medium text-center">{{skipSequence.current}}</div>
                            </div>
                            <div>
                                <div class="grey--text mb-2 text-center">&nbsp;</div>
                                <div class="font-weight-medium text-center">=</div>
                            </div>
                            <div>
                                <div class="grey--text text-darken-1 mb-2 text-center">Expected</div>
                                <div class="font-weight-medium text-center">{{skipSequence.expected}}</div>
                            </div>
                        </div>
                    </v-col>
                </v-row>
            </v-card-text>
            <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="blue darken-1" text @click="showSkipDialog = false">Close</v-btn>
                <v-btn color="blue darken-1" text @click="skip">Skip</v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
    <v-dialog v-model="showLoadDialog" max-width="600px">
        <v-card>
            <v-card-title class="headline lighten-2">
                Confirm
            </v-card-title>
            <v-card-text>
                Please confirm whether to load the sequence <span style="background: #e0e0e0; word-break: break-all;">{{loadSequence.key}}</span> into memory.
            </v-card-text>
            <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="blue darken-1" text @click="showLoadDialog = false">Close</v-btn>
                <v-btn color="blue darken-1" text @click="load(loadSequence.key)">Load</v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
    <v-snackbar top right v-model="snackbar" color="error" timeout="3000">
        {{ message }}
        <template v-slot:action="{ attrs }">
            <v-btn icon v-bind="attrs" @click="snackbar = false">
                <v-icon>mdi-close</v-icon>
            </v-btn>
        </template>
    </v-snackbar>
</div>`,
    data() {
        return {
            mode: null,
            standalone: false,
            loading: false,
            showExpand: false,
            singleExpand: false,
            expanded: [],
            search: '',
            message: '',
            snackbar: false,
            showSaveDialog: false,
            showUpdateDialog: false,
            showDeleteDialog: false,
            showSkipDialog: false,
            showLoadDialog: false,
            rules: {
                key: [
                    value => (value != null && value != '') || "Key is required",
                ],
                value: [
                    value => value != null || "Value is required",
                    value => value >= 0 || 'Must be greater than 0',
                ],
                increment: [
                    value => value != null || "Increment is required",
                    value => value > 0 || 'Must be greater than 1',
                ],
                cache: [
                    value => value != null || "Cache size is required",
                    value => value > 0 || 'Must be greater than 1',
                ],
                size: [
                    value => value != null || "Size is required",
                    value => value > 0 || 'Must be greater than 0',
                ],
            },
            headers: [
                {text: 'Key', value: 'key', align: 'start'},
                {text: 'Value', value: 'value', align: 'start', filterable: false},
                {text: 'Increment', value: 'increment', align: 'start', filterable: false},
                {text: 'Cache Size', value: 'cache', align: 'start', filterable: false},
                {text: 'Version', value: 'version', align: 'start', filterable: false},
                {text: 'Create Time', value: 'createTime', align: 'start'},
                {text: 'Update Time', value: 'updateTime', align: 'start'},
                {text: 'Operation', value: 'operation', align: 'start', filterable: false},
            ],
            sequences: [],
            saveSequence: {
                key: null,
                value: 0,
                increment: 1,
                cache: 5000,
            },
            updateSequence: {
                key: null,
                increment: 1,
                cache: 5000,
            },
            deleteSequence: {
                key: null,
                input: null,
            },
            skipSequence: {
                key: null,
                size: 0,
            },
            loadSequence: {
                key: null,
            },
        };
    },
    methods: {
        list() {
            this.loading = true;
            api.console.getMode(null, (result) => {
                if (!result.success) {
                    this.loading = false;
                    this.toast(result.message);
                    return;
                }
                this.mode = result.data;
                if (this.mode === 'STANDALONE') {
                    this.showExpand = true;
                    this.standalone = true;
                }
                api.console.list(null, (result) => {
                    if (!result.success) {
                        this.loading = false;
                        this.toast(result.message);
                        return;
                    }
                    let list = result.data.list;
                    for (let i in list) {
                        let seq = list[i];
                        this.warp(seq);
                    }
                    this.sort(list);
                    this.sequences = list;
                    this.loading = false;
                });
            });
        },
        get(key) {
            let sequence = this.getByKey(key);
            let present = true;
            if (!sequence) {
                sequence = {};
                present = false;
            }
            sequence.loading = true;
            api.console.get({key}, (result) => {
                sequence.loading = false;
                if (!result.success) {
                    this.toast(result.message);
                    return;
                }
                if (!result.data) {
                    return;
                }
                sequence.key = result.data.key;
                sequence.value = result.data.value;
                sequence.increment = result.data.increment;
                sequence.cache = result.data.cache;
                sequence.version = result.data.version;
                sequence.createTime = result.data.createTime;
                sequence.updateTime = result.data.updateTime;
                if (!present) {
                    this.warp(sequence);
                    this.sequences.unshift(sequence);
                }
            });
        },
        remove(key) {
            api.console.delete({key: key.trim()}, (result) => {
                if (!result.success) {
                    this.showDeleteDialog = false;
                    this.toast(result.message);
                    return;
                }
                this.delByKey(key);
                this.showDeleteDialog = false;
            });
        },
        save() {
            for (let k in this.saveSequence) {
                for (let i in this.rules[k]) {
                    let r = this.rules[k][i](this.saveSequence[k]);
                    if (r !== true) {
                        console.log(r);
                        return;
                    }
                }
            }
            this.saveSequence.key = this.saveSequence.key.trim();
            let sequence = this.getByKey(this.saveSequence.key);
            if (sequence) {
                this.toast("Sequence '" + sequence.key + "' already exists");
                return;
            }
            api.console.save(this.saveSequence, (result) => {
                if (!result.success) {
                    this.toast(result.message);
                    return;
                }
                let seq = {
                    key: this.saveSequence.key,
                    value: this.saveSequence.value,
                    increment: this.saveSequence.increment,
                    cache: this.saveSequence.cache,
                    version: 1,
                    createTime: this.format(new Date()),
                };
                this.warp(seq);
                this.sequences.unshift(seq);
                this.get(this.saveSequence.key);
                this.showSaveDialog = false;
            });
        },
        update() {
            for (let k in this.updateSequence) {
                for (let i in this.rules[k]) {
                    let r = this.rules[k][i](this.updateSequence[k]);
                    if (r !== true) {
                        console.log(r);
                        return;
                    }
                }
            }
            let sequence = this.getByKey(this.updateSequence.key);
            if (!sequence) {
                return;
            }
            if (sequence.increment == this.updateSequence.increment
                && sequence.cache == this.updateSequence.cache) {
                // Skips update.
                this.showUpdateDialog = false;
                return;
            }
            api.console.update(this.updateSequence, (result) => {
                if (!result.success) {
                    this.toast(result.message);
                    return;
                }
                this.get(this.updateSequence.key);
                this.showUpdateDialog = false;
            });
        },
        getSnapshot(key) {
            let sequence = this.getByKey(key);
            if (!sequence) {
                return;
            }
            sequence.loadingSnapshot = true;
            api.console.getSnapshot({key}, (result) => {
                if (!result.success) {
                    sequence.loadingSnapshot = false;
                    this.toast(result.message);
                    return;
                }
                if (result.data != null) {
                    sequence.snapshot = result.data;
                    sequence.hasSnapshot = true;
                }
                sequence.loadingSnapshot = false;
            });
        },
        load(key) {
            let sequence = this.getByKey(key);
            if (!sequence) {
                return;
            }
            sequence.loadingSnapshot = true;
            api.console.load({key}, (result) => {
                if (!result.success) {
                    this.toast(result.message);
                    return;
                }
                this.getSnapshot(key);
            });
        },
        skip() {
            for (let k in this.skipSequence) {
                for (let i in this.rules[k]) {
                    let r = this.rules[k][i](this.skipSequence[k]);
                    if (r !== true) {
                        console.log(r);
                        return;
                    }
                }
            }
            let sequence = this.getByKey(this.skipSequence.key);
            if (!sequence) {
                return;
            }
            let params = {
                key: this.skipSequence.key,
                size: this.skipSequence.size,
            };
            api.console.skip(params, (result) => {
                if (!result.success) {
                    this.toast(result.message);
                    return;
                }
                if (this.standalone) {
                    this.getSnapshot(this.skipSequence.key)
                } else {
                    this.get(this.skipSequence.key)
                }
                this.showSkipDialog = false;
            });
        },
        changeSkipSize() {
            if (this.skipSequence.size && !isNaN(this.skipSequence.size)) {
                this.skipSequence.expected = parseInt(this.skipSequence.size) * this.skipSequence.increment + parseInt(this.skipSequence.current);
            } else {
                this.skipSequence.size = '?';
                this.skipSequence.expected = '?';
            }
        },
        sort(list) {
            list.sort(function (a, b) {
                if (!a.createTime) {
                    return 1;
                }
                if (!b.createTime) {
                    return -1;
                }
                return b.createTime.localeCompare(a.createTime);
            });
        },
        warp(seq) {
            seq.loading = false;
            seq.hasSnapshot = false;
            seq.loadingSnapshot = false;
            seq.snapshot = {};
        },
        openSaveDialog() {
            this.saveSequence = {
                key: null,
                value: 0,
                increment: 1,
                cache: 5000,
            }
            this.showSaveDialog = true;
        },
        openUpdateDialog(key) {
            let sequence = this.getByKey(key);
            if (!sequence) {
                return;
            }
            this.updateSequence = {
                key: sequence.key,
                increment: sequence.increment,
                cache: sequence.cache,
            }
            this.showUpdateDialog = true;
        },
        openDeleteDialog(key) {
            this.deleteSequence = {
                key: key,
                input: null,
            };
            this.showDeleteDialog = true;
        },
        openSkipDialog(key) {
            let sequence = this.getByKey(key);
            if (!sequence) {
                return;
            }
            if (this.standalone) {
                this.skipSequence = {
                    key: sequence.key,
                    size: null,
                    current: sequence.snapshot.cur,
                    expected: sequence.snapshot.cur,
                    increment: sequence.snapshot.increment,
                };
            } else {
                this.skipSequence = {
                    key: sequence.key,
                    size: null,
                    current: sequence.value,
                    expected: sequence.value,
                    increment: sequence.increment,
                };
            }
            this.showSkipDialog = true;
        },
        openLoadDialog(key) {
            let sequence = this.getByKey(key);
            if (!sequence) {
                return;
            }
            this.loadSequence = {
                key: sequence.key,
            };
            this.showLoadDialog = true;
        },
        itemExpanded(data) {
            if (data.value && !data.item.hasSnapshot) {
                this.getSnapshot(data.item.key);
            }
        },
        expandAll() {
            for (let i in this.sequences) {
                this.expanded.push(this.sequences[i]);
            }
        },
        collapseAll() {
            this.expanded = [];
        },
        toast(msg) {
            this.message = msg;
            this.snackbar = true;
        },
        getByKey(key) {
            let sequence = null;
            for (let i in this.sequences) {
                if (this.sequences[i].key == key) {
                    sequence = this.sequences[i];
                    break;
                }
            }
            return sequence;
        },
        delByKey(key) {
            for (let i in this.sequences) {
                if (this.sequences[i].key == key) {
                    this.sequences.splice(i, 1);
                    break;
                }
            }
        },
        format(date) {
            let pattern = "yyyy-MM-dd HH:mm:ss";
            let o = {
                "M+": date.getMonth() + 1,
                "d+": date.getDate(),
                "H+": date.getHours(),
                "m+": date.getMinutes(),
                "s+": date.getSeconds(),
                "q+": Math.floor((date.getMonth() + 3) / 3),
                "S": date.getMilliseconds()
            };
            if (/(y+)/.test(pattern)) {
                pattern = pattern.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
            }
            for (let k in o) {
                if (new RegExp("(" + k + ")").test(pattern)) {
                    pattern = pattern.replace(RegExp.$1, (RegExp.$1.length === 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length)));
                }
            }
            return pattern;
        },
        init() {
            this.list();
        },
    },
    created() {
        this.init();
    },
};
const router = new VueRouter({
    routes: [
        {name: '/', path: '/', component: monitor},
        {name: 'monitor', path: '/monitor', component: monitor},
    ]
});
const vm = new Vue({
    el: '#app',
    router: router,
    vuetify: new Vuetify(),
});

function $get(uri, params, callback) {
    handler(axios.get(uri, {params}), callback);
}

function $post(uri, params, callback) {
    handler(axios.post(uri, params), callback);
}

function $put(uri, params, callback) {
    handler(axios.put(uri, params), callback);
}

function $patch(uri, params, callback) {
    handler(axios.patch(uri, params), callback);
}

function $delete(uri, params, callback) {
    handler(axios.delete(uri, {params}), callback);
}

function handler(o, callback) {
    o.then((result) => {
        if (callback) {
            callback(result.data);
        }
    }).catch((error) => {
        if (callback) {
            callback({
                success: false,
                message: error.response.statusText
            });
        }
    });
}